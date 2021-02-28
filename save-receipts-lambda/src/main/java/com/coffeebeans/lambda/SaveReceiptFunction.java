package com.coffeebeans.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import java.util.HashMap;
import java.util.Map;

public class SaveReceiptFunction implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  private static final AmazonS3 S3_CLIENT = AmazonS3ClientBuilder.defaultClient();
  private static final String BUCKET_NAME = "digital-receipts-dev";

  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    final LambdaLogger logger = context.getLogger();

    final Map<String, String> headers = createResponseHeaderMap(context);

    final String objectKey = String.format("%s.json", context.getAwsRequestId());
    final String content = input.getBody();

    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
        .withHeaders(headers);
    try {

      S3_CLIENT.putObject(BUCKET_NAME, objectKey, content);

      return response
          .withStatusCode(202);
    } catch (final Exception e) {
      logger.log(e.getLocalizedMessage());
      return response
          .withStatusCode(500);
    }
  }

  private Map<String, String> createResponseHeaderMap(Context context) {
    final Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    headers.put("Location", String.format("/receipt/%s", context.getAwsRequestId()));
    return headers;
  }
}