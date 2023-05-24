# backup
    ```shell
    # to remove

    tar -zcvf labs-apim-app.tar.gz ../app

    scp -i ${SSH_KEY_PATH} -J adminuser@jumphost-vm-linux.${DOMAIN} \
        labs-apim-app.tar.gz adminuser@apim-wl-vm-linux:

    scp -i ${SSH_KEY_PATH} -J adminuser@jumphost-vm-linux.${DOMAIN} \
        labs-apim-app.tar.gz adminuser@appgw-wl-vm-linux:

    #
    tar -zcvf ${DOMAIN}.tar.gz ${DOMAIN}
    scp -i ${SSH_KEY_PATH} -J adminuser@jumphost-vm-linux.${DOMAIN} \
    ${DOMAIN}.tar.gz adminuser@appgw-wl-vm-linux:
    ```

## run 
```shell
export REMOTE=apim-wl-vm-linux or appgw-wl-vm-linux
ssh -i $(SSH_KEY_PATH) -J adminuser@jumphost-vm-linux.$(DOMAIN) adminuser@${REMOTE}

# on server ${REMOTE}
tar -xvf  labs-apim-app.tar.gz
cd app
nohup ./mvnw clean compile spring-boot:run > spring-boot.run.out 2>&1 &
```




### nginx on appgw-wl-vm-linux
```shell
ssh -i $(SSH_KEY_PATH) -J adminuser@jumphost-vm-linux.$(DOMAIN) adminuser@appgw-wl-vm-linux
# on server appgw-wl-vm-linux
export DOMAIN=xxxx
sudo nano /etc/nginx/sites-available/${DOMAIN}
```
with content

```shell
#server {
#    listen 80;
#    return 301 https://$host$request_uri;
#}

server {

    server_name ${DOMAIN};
    listen 443 ssl;

    # ssl certificate
    ssl_certificate     /home/adminuser/letsencrypt/live/${DOMAIN}/fullchain.pem;
    ssl_certificate_key /home/adminuser/letsencrypt/live/${DOMAIN}/privkey.pem;


    location / {
          proxy_pass http://localhost:8080;
          proxy_http_version 1.1;
          proxy_set_header Upgrade $http_upgrade;
          proxy_set_header Connection keep-alive;
          proxy_set_header Host              $host;
          proxy_set_header X-Forwarded-For   $proxy_add_x_forwarded_for;
          proxy_set_header X-Real-IP         $remote_addr;
          proxy_set_header X-Forwarded-Host  $host;
          proxy_set_header X-Forwarded-Proto $scheme;
          proxy_set_header X-Forwarded-Port  $server_port;
          proxy_cache_bypass $http_upgrade;
    }
 }
```
```shell
sudo ln -s /etc/nginx/sites-available/${DOMAIN} /etc/nginx/sites-enabled/
sudo nginx -t
```
