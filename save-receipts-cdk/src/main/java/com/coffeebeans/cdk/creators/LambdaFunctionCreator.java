package com.coffeebeans.cdk.creators;

import static lombok.AccessLevel.PRIVATE;

import java.util.Map;
import lombok.NoArgsConstructor;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Tags;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.lambda.AssetCode;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Function.Builder;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

@NoArgsConstructor(access = PRIVATE)
public final class LambdaFunctionCreator {

  private static final int MEMORY_SIZE = 512;

  public static Function createFunction(final Construct scope,
      final String id,
      final String handler,
      final Runtime runtime,
      final AssetCode code,
      final Vpc vpc) {


    return createFunction(scope, id, handler, runtime, code, vpc, null);
  }

  public static Function createFunction(final Construct scope,
      final String id,
      final String handler,
      final Runtime runtime,
      final AssetCode code,
      final Vpc vpc,
      final Map<String, String> environment) {

    final Function function = Builder.create(scope, id)
        .runtime(runtime)
        .code(code)
        .handler(handler)
        .vpc(vpc)
        .memorySize(MEMORY_SIZE)
        .vpcSubnets(SubnetSelection.builder().subnetType(SubnetType.PRIVATE).build())
        .environment(environment)
        .timeout(Duration.seconds(10))
        .build();

    Tags.of(function).add("env", "dev");

    return function;
  }
}
