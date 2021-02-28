package com.coffeebeans.cdk.creators;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import java.util.Objects;
import lombok.NoArgsConstructor;
import software.amazon.awscdk.core.Tags;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.LambdaRestApi.Builder;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.lambda.Function;
import software.constructs.Construct;

@NoArgsConstructor(access = PRIVATE)
public final class LambdaRestApiCreator {

  public static LambdaRestApi createLambdaRestApi(final Construct scope,
      final Function function,
      final String endpoint,
      final String stageName,
      final boolean proxy,
      final String resourceName,
      final List<String> httpMethods) {

    // point to the lambda
    final LambdaRestApi lambdaRestApi = Builder.create(scope, endpoint)
        .handler(function)
        .proxy(proxy)
        .restApiName(endpoint)
        .deployOptions(StageOptions.builder().stageName(stageName).build())
        .build();

    // get root resource to add methods
    final Resource resource = lambdaRestApi.getRoot()
        .addResource(resourceName);

    // add methods
    httpMethods
        .stream()
        .filter(Objects::nonNull)
        .map(String::toUpperCase)
        .forEach(resource::addMethod);

    Tags.of(lambdaRestApi).add("env", "dev");
    Tags.of(resource).add("env", "dev");

    return lambdaRestApi;
  }
}
