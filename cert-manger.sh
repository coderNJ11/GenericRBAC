kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.11.0/cert-manager.yaml
kubectl apply -f issuers.yaml # Declare Let's Encrypt issuer for cert-manager