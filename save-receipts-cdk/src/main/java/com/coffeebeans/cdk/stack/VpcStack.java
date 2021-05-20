package com.coffeebeans.cdk.stack;

import static com.coffeebeans.cdk.creators.LambdaFunctionCreator.createFunction;
import static com.coffeebeans.cdk.creators.LambdaRestApiCreator.createLambdaRestApi;
import static com.coffeebeans.cdk.creators.S3BucketCreator.createBucket;
import static com.google.common.collect.Lists.newArrayList;
import static software.amazon.awscdk.services.lambda.Code.fromAsset;
import static software.amazon.awscdk.services.lambda.Runtime.JAVA_11;
import static software.amazon.awscdk.services.s3.BlockPublicAccess.BLOCK_ALL;
import static software.amazon.awscdk.services.s3.BucketEncryption.S3_MANAGED;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.core.Tags;
import software.amazon.awscdk.services.ec2.NatProvider;
import software.amazon.awscdk.services.ec2.SubnetConfiguration;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.lambda.AssetCode;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.s3.Bucket;

public class VpcStack extends Stack {

  private static final String CIDR = "10.16.0.0/16";
  private static final int CIDR_MASK = 20;

  public VpcStack(final Construct parent, final String id) {
    this(parent, id, null);
  }

  public VpcStack(final Construct parent, final String id, final StackProps props) {
    super(parent, id, props);
    final String env = System.getenv("ENV");
    final String vpcId = String.format("receipts-%s-vpc", env);

    final Vpc vpc = Vpc.Builder.create(this, vpcId)
        .cidr(CIDR)
        .subnetConfiguration(createSubnetConfigurations())
        .natGateways(1)
        .natGatewayProvider(NatProvider.gateway())
        .build();

    Tags.of(vpc).add("env", env);

    final Function saveReceiptsHandler = createLambdaFunction(vpc, env);

    final Bucket bucket = createBucket(this, String.format("digital-receipts-%s", env), BLOCK_ALL, S3_MANAGED, false, false);

    bucket.grantPut(Objects.requireNonNull(saveReceiptsHandler.getRole()));

    createLambdaRestApi(this, saveReceiptsHandler, "ReceiptsEndpoint", "dev", false, "receipt", Lists.newArrayList("POST"));
  }

  private Function createLambdaFunction(final Vpc vpc, final String env) {
    final String saveReceiptsHandler = "SaveReceiptsHandler";
    final String handler = "com.coffeebeans.lambda.SaveReceiptFunction";
    final AssetCode assetCode = fromAsset("save-receipts-lambda/target/save-receipts-lambda-0.0.1-SNAPSHOT.jar");
    final Function saveReceiptsFunction = createFunction(this, saveReceiptsHandler, handler, JAVA_11, assetCode, vpc);

    Tags.of(saveReceiptsFunction).add("env", env);
    return saveReceiptsFunction;
  }

  private List<? extends SubnetConfiguration> createSubnetConfigurations() {
    final SubnetConfiguration zoneAReceiptPublicSubnet = new SubnetConfiguration.Builder()
        .cidrMask(CIDR_MASK)
        .subnetType(SubnetType.PUBLIC)
        .name("receipt-public")
        .build();

    final SubnetConfiguration zoneBReceiptPublicSubnet = new SubnetConfiguration.Builder()
        .cidrMask(CIDR_MASK)
        .subnetType(SubnetType.PRIVATE)
        .name("receipt-private")
        .build();

    return newArrayList(zoneAReceiptPublicSubnet, zoneBReceiptPublicSubnet);
  }
}
