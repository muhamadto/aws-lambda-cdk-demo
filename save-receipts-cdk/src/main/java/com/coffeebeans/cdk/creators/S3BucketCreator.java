package com.coffeebeans.cdk.creators;

import java.security.SecureRandom;
import java.util.function.Supplier;
import software.amazon.awscdk.core.PhysicalName;
import software.amazon.awscdk.services.iam.Grant;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketEncryption;
import software.constructs.Construct;

public class S3BucketCreator {

  public static Bucket createBucket(final Construct scope,
      final String id,
      final BlockPublicAccess blockAll,
      final BucketEncryption encryption,
      final boolean publicReadAccess,
      final boolean versioned) {
    return Bucket.Builder.create(scope, id)
        .blockPublicAccess(blockAll)
        .encryption(encryption)
        .publicReadAccess(publicReadAccess)
        .versioned(versioned)
        .bucketName(id)
        .build();
  }
}
