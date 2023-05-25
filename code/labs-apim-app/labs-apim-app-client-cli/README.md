# labs-apim-app-client-cli

## run 

```shell
source ../../env.vars.sh

./mvnw clean package

# with secret
java -jar target/*.jar secret

# with certificate
java -jar target/*.jar certificate

```


## prepare

```shell
source ../../env.vars.sh

rm labs-apim-app-client-cli.* 

# generate private key
openssl genrsa -out labs-apim-app-client-cli.key 4098 

# 
openssl pkcs8 -topk8 -inform PEM -outform PEM -in labs-apim-app-client-cli.key \
    -out labs-apim-app-client-cli.pem -nocrypt

# generate a certificate request
openssl req -new \
  -subj "/C=IT/CN=labs-apim-app-client-cli/emailAddress=admin@$DOMAIN" \
  -key labs-apim-app-client-cli.key \
  -out labs-apim-app-client-cli.csr

# generate the certificate
openssl x509 -req -days 365 \
        -signkey labs-apim-app-client-cli.key \
        -in labs-apim-app-client-cli.csr \
        -out labs-apim-app-client-cli.crt


openssl genrsa -out labs-apim-app-client-cli.key 4098

openssl \
  req \
  -nodes \
  -newkey rsa:2048 \
  -keyout labs-apim-app-client-cli.key \
  -out labs-apim-app-client-cli.csr \
  -subj "/C=IT/CN=labs-apim-app-client-cli/emailAddress=admin@$DOMAIN"
```

