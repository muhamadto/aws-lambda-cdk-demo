package com.coffeebeans.cdk.creators;

import software.amazon.awscdk.core.Tags;
import software.amazon.awscdk.services.ec2.Subnet;
import software.amazon.awscdk.services.ec2.SubnetProps;
import software.constructs.Construct;

public class SubnetCreator {

  public static Subnet createSubnet(final Construct scope,
      final String id,
      final String availabilityZone,
      final String cidrBlock,
      final String vpcId,
      final boolean mapPublicIpOnLaunch) {

    return new Subnet(scope, id, SubnetProps.builder()
        .availabilityZone(availabilityZone)
        .cidrBlock(cidrBlock)
        .vpcId(vpcId)
        .mapPublicIpOnLaunch(mapPublicIpOnLaunch)
        .build());
  }
}
