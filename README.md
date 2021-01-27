# Garrar


##Installation Guide

```
git clone [PROJECT]
```

Delete ```.git``` folder in Project folder

Rename Package using refactor


## Getting Started

Build.gradle
```
applicationId "aTrip.user"
resValue "string", "app_name", "Cabbie"
resValue "string", "google_map_key", "AIzaSyD3KBp61QYDt0gDB4EON_ihClCxqPTfEkA"
resValue "string", "FACEBOOK_APP_ID", "440549846626247"
resValue "string", "FACEBOOK_LOGIN_PROTOCOL_SCHEME", "fb440549846626247"
resValue "string", "ACCOUNT_KIT_CLIENT_TOKEN", "bd15bdc9cf953d7a2419ccbe4930bd44"
resValue "string", "default_notification_channel_id", "fcm_default_channel"
resValue "string", "google_signin_server_client_id", "1040987739433-655var9p02e456t6064l42bcrf09v3on.apps.googleusercontent.com"

buildConfigField "String", "BASE_URL", '"https://cabbieuae.com/"'
buildConfigField "String", "BASE_IMAGE_URL", '"https://cabbieuae.com/storage/"'
buildConfigField "String", "CLIENT_SECRET", '"yEZPnjkdrIOVILo43t2DgRVb90oPjp9CZEmXAxb0"'
buildConfigField "String", "TERMS_CONDITIONS", '"https://cabbieuae.com/"'

buildConfigField "String", "DRIVER_PACKAGE", '"aTrip.pilot"'
buildConfigField "String", "CLIENT_ID", '"2"'
buildConfigField "String", "DEVICE_TYPE", '"android"'
buildConfigField "String", "PAYPAL_CLIENT_TOKEN", '"eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiJlMWMzMGE4YmJlZmZlODk4MDJlMmY2ZGM5MzE4NjE1ZmJmZDQ4YWFjMTc3Y2ZkN2YxZjE4MDc1YjMzMzFkYmQ2fGNyZWF0ZWRfYXQ9MjAxOC0wOC0wN1QwNjo0MDoxMS4zNjI0Nzg4MzIrMDAwMFx1MDAyNm1lcmNoYW50X2lkPTM0OHBrOWNnZjNiZ3l3MmJcdTAwMjZwdWJsaWNfa2V5PTJuMjQ3ZHY4OWJxOXZtcHIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzLzM0OHBrOWNnZjNiZ3l3MmIvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vb3JpZ2luLWFuYWx5dGljcy1zYW5kLnNhbmRib3guYnJhaW50cmVlLWFwaS5jb20vMzQ4cGs5Y2dmM2JneXcyYiJ9LCJ0aHJlZURTZWN1cmVFbmFibGVkIjp0cnVlLCJwYXlwYWxFbmFibGVkIjp0cnVlLCJwYXlwYWwiOnsiZGlzcGxheU5hbWUiOiJBY21lIFdpZGdldHMsIEx0ZC4gKFNhbmRib3gpIiwiY2xpZW50SWQiOm51bGwsInByaXZhY3lVcmwiOiJodHRwOi8vZXhhbXBsZS5jb20vcHAiLCJ1c2VyQWdyZWVtZW50VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3RvcyIsImJhc2VVcmwiOiJodHRwczovL2Fzc2V0cy5icmFpbnRyZWVnYXRld2F5LmNvbSIsImFzc2V0c1VybCI6Imh0dHBzOi8vY2hlY2tvdXQucGF5cGFsLmNvbSIsImRpcmVjdEJhc2VVcmwiOm51bGwsImFsbG93SHR0cCI6dHJ1ZSwiZW52aXJvbm1lbnROb05ldHdvcmsiOnRydWUsImVudmlyb25tZW50Ijoib2ZmbGluZSIsInVudmV0dGVkTWVyY2hhbnQiOmZhbHNlLCJicmFpbnRyZWVDbGllbnRJZCI6Im1hc3RlcmNsaWVudDMiLCJiaWxsaW5nQWdyZWVtZW50c0VuYWJsZWQiOnRydWUsIm1lcmNoYW50QWNjb3VudElkIjoiYWNtZXdpZGdldHNsdGRzYW5kYm94IiwiY3VycmVuY3lJc29Db2RlIjoiVVNEIn0sIm1lcmNoYW50SWQiOiIzNDhwazljZ2YzYmd5dzJiIiwidmVubW8iOiJvZmYifQ=="'

```

[google_signin_server_client_id](https://console.cloud.google.com/) -API&Services -> Credentials -> OAuth 2.0 client IDs -> Web client (auto created by Google Service)



Add app in [firebase](http://console.firebase.google.com/), and don't forgot to add SHA-1

[Firebase console](http://console.firebase.google.com/) -Realtime Database -> set database rules as below
```
{
  "rules": {
    ".read": true,
    ".write": true
  }
}
```



