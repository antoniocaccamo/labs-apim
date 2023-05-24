#!/bin/sh

echo ""
echo settings env vars..

export     TENANT_ID="53695c6a-...............................cfd8b0"
export     SERVER_ID="1b5a6020-...............................deb5ff"
export     CLIENT_ID="254207e6-...............................c2319a"
export CLIENT_SECRET="lMW8Q~3f1...............................poL3ni_dcf"
export       SUB_KEY="a5300366b...............................cd"
export         SCOPE="$SERVER_ID/.default"
export        DOMAIN="ant........................it"

export SSH_KEY_PATH=$HOME/.ssh/azure-vm

# cli env 
export ENDPOINT=https://api.$DOMAIN/external/api/books