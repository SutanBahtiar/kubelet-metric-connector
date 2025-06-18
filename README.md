## Kubelet Metrics Connector
___

```
# create service account & rule
kubectl apply -f prometheus-kubelet.yaml

# create token duration 8760h=1year
kubectl create token prometheus-kubelet -n monitoring --duration=8760h > prometheus-kubelet-token

# test
curl -k \
        -H "Authorization: Bearer $(cat prometheus-kubelet-token)" \
        https://localhost:10250/metrics
        
# reverse proxy
location /kubelet-metrics/ {
        https://localhost:9075/kubelet-metrics/
}        


# reverse proxy
set $sandbox_token tokenvalue;

location /sandbox/ {
        proxy_set_header Authorization "Bearer $sandbox_token";
        proxy_pass https://localhost:10250/;
        proxy_ssl_verify off;
}
