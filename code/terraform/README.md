# lab-apim terraform

## steps

1. setup azure subscription
   
   ```shell
   source ../main.sh
   ```
   
3. verify code with checkov
   
   ```shell
   python3 -m venv .venv
   source .venv/bin/activate
   python -m pip install checkov
   checkov --quiet -d infrastructure -d network ... 
   ```

2. then launch terraform 
   
    ```shell
    terraform init
    terraform plan -out=appgw-apim.tfplan
    terraform apply appgw-apim.tfplan
    ```

3. access
   
   ```shell
   export DOMAIN=xxxxxx
   export SSK_KEY_PATH=xxxx

   # connect to apim-wl-vm-linux
   ssh -i $(SSK_KEY_PATH) -J adminuser@jumphost-vm-linux.$(DOMAIN) adminuser@apim-wl-vm-linux

   # connect to appgw-wl-vm-linux
   ssh -i $(SSK_KEY_PATH) -J adminuser@jumphost-vm-linux.$(DOMAIN) adminuser@appgw-wl-vm-linux
   ```

4. let's encrypt
   
   ```shell
   docker run -it --rm --name certbot \
      -v "./etc/letsencrypt:/etc/letsencrypt" \
      -v "./var/lib/letsencrypt:/var/lib/letsencrypt" \
      certbot/certbot certonly --manual -d *.$DOMAIN -d $DOMAIN \
      --agree-tos --manual-public-ip-logging-ok --preferred-challenges dns \
      --server https://acme-v02.api.letsencrypt.org/directory \
      --register-unsafely-without-email --rsa-key-size 4096
   ```

5. after DNS challenge, here the certs
   
   ```shell
   sudo chown -R $USER:$USER var etc
   # certificates are
   ls -l etc/letsencrypt/live/$DOMAIN

   openssl pkcs12 -inkey privkey.pem  -in fullchain.pem  -export -out $DOMAIN.pfx
   ```


