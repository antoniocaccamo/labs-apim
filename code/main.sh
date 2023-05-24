#!/bin/sh

set -e

# Create Resource Group
echo "subscriptions list"

az account list -o table

#read -p "enter subscription id: " subscription_id

#az account set -s $subscription_id