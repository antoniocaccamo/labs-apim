# lab-apim software :  app-server

Springboot Application that acts as Resource Reserve

## 





## run locally

```shell
source ../../env.vars.sh 

./mvnw clean compile spring-boot:run

```

## prepare for trasfer

1. local
    ```shell
    source ../../env.vars.sh 
    ./mvnw clean
    cd ..
    rm labs-apim-app-server.tar.gz
    tar -zcvf labs-apim-app-server.tar.gz labs-apim-app-server
    scp -i ${SSH_KEY_PATH}  labs-apim-app-server.tar.gz azureuser@jumphost-vm-linux.${DOMAIN}:workspaces
    # log in jumphost
    ssh -i ${SSH_KEY_PATH}  azureuser@jumphost-vm-linux.${DOMAIN}
    ```

1. transfer to jumphost-vm-linux
    ```shell

    cd workspaces
    rm -rf labs-apim-app-server

    export SUBSCRIPTION_ID=908894................
    ./mvnw -V -Pazure package azure-webapp:deploy
    ```



#

## links 
1. https://www.digitalocean.com/community/tutorials/how-to-install-nginx-on-ubuntu-20-04
2. [Reference](https://learn.microsoft.com/en-us/azure/developer/java/spring-framework/spring-security-support?tabs=SpringCloudAzure5x#accessing-a-resource-server)
1. [aad-resource-server](https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/aad/spring-cloud-azure-starter-active-directory/web-client-access-resource-server/aad-resource-server/README.md)
- ![x](https://learn.microsoft.com/en-us/azure/developer/java/spring-framework/media/spring-cloud-azure/system-diagram-stand-alone-resource-server-usage.png)