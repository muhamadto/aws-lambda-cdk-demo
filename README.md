Simple lambda that accepts an JSON object (receipt model) and save it to S3. The lambda itself was is not the focus of this project. Rather, I wanted to focus on how to deploy the lambda using aws CDK.

## Directory Structure
```
save-receipt-function
  save-receipts-cdk
    src
      main
        java
          com.coffeebeans.cdk
  save-receipts-lambda
    src
      main
        java
          lambda
```

## aws setup
follow these steps to create the stack in the development account (************)

### ~/.aws/credentials Changes
Append your `~/.aws/credentials` with the following snippet
```shell
[receipts-dev]
aws_access_key_id=your_aws_access_key_id
aws_secret_access_key=your_aws_secret_access_key
region=ap-southeast-2
```

### ~/.aws/config  Changes
Append your `~/.aws/config` with the following snippet

```shell
[profile receipts-dev]
region=ap-southeast-2
output=json
account=::************ #main account
role_arn=arn:aws:iam::************:role/OrganizationAccountAccessRole
source_profile=default
role_session_name=your_preferred_session_name
```

## Deployment
```shell
cd /path/to/save-receipt-function
mvn clean package  
cd save-receipts-cdk
cdk bootstrap --profile receipts-dev
cdk deploy --profile receipts-dev
```

## What will this create
* VPC
* 4 subnets (two private and two public)
* Nat gateway
* S3 bucket
* Lambda function to save files in S3
* Api gateway endpoint to allow clients call lambda as a rest endpoint.
* Roles and policies to allow RestApi call lambda and lambda to put objects in S3


## Useful commands

* `mvn package`     compile and run tests
* `cdk ls`          list all stacks in the app
* `cdk synth`       emits the synthesized CloudFormation template
* `cdk deploy`      deploy this stack to your default AWS account/region
* `cdk diff`        compare deployed stack with current state
* `cdk docs`        open CDK documentation
