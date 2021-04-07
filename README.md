# Identifo Android
Identifo Android is an open library that allows you instantly and easily integrate the [Identifo](https://github.com/MadAppGang/identifo) authentication framework.

## Structure
The library includes two modules:
1. Core module is responsible for basic logic, queries and tokens managing.
2. UI module includes basic authentication use cases which might help you build app quickly.

## Installation
To install library you should do two steps:
1. Specify the Jitpack repository in the `build.gradle` file of your root project.
```javascript
allprojects {
    repositories {
	   ...
	   maven { url 'https://jitpack.io' }
    }
}
```
2. Add dependencies to your `build.gradle` subproject file.
```javascript
def identifoVersion = "x.y.z"
implementation "com.github.MadAppGang.identifo-android:core:${identifoVersion}"
    
// Optional 
implementation "com.github.MadAppGang.identifo-android:ui:${identifoVersion}"
```

## Usage
#### Step 1
Initialize the library and pass application access identifier, HMAC shared secret key and Identifo URL.
```javascript
class IdentifoDemoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val appID = "applicationID"
        val secret = "sharedSecret"
        val baseUrl = "IdentifoUrl"

        IdentifoAuthentication.initAuthenticator(this, baseUrl, appID, secret)
    }
}
```
*Note that it is better to store keys and IDs in the `local.properties` file or other files ignored by version control system.*
#### Step 2
Select the action you need. For example, a sign in might look like this:
```javascript
viewModelScope.launch {
    IdentifoAuthentication.loginWithUsernameAndPassword(
        username = "username",
        password = "password"
    ).onSuccess { loginResult ->
         // request performed successfully
    }.onError { error ->
         // request failure
    }
}
```
#### Step 3
Observe the current user authentication status: 
```javascript
IdentifoAuthentication.authenticationState.asLiveData().observe(this) { state ->
    when (state) {
        is Authentificated -> {
            val accessToken = state.accessToken
            val user = state.identifoUser
            // user is authenticated successfully
        }
        else -> {
            // user is deauthenticated
        }
    }
}
```

#### Step 4 (Optional)
If you don't need a complex design, you can use a dedicated interface which would be enough for common cases. There are sign in and sign up flows.
Sign in flow using:
```javascript
val style = Style(
    companyLogo = R.drawable.ic_logo,
    companyName = "Company/Applicaion",
    greetingsText = "Greetings text"
)

val userConditions = UseConditions(
    userAgreement = "https://userAgreement.com/",
    privacyPolicy = "https://privacyPolicy.com/"
)

val providers = listOf(EMAIL, PHONE, FACEBOOK)

val loginOptions = LoginOptions(
    commonStyle = style,
    providers = providers,
    useConditions = userConditions
)

IdentifoSignInActivity.openActivity(this, loginOptions)
```
The registration flow that you can easily use is as follows:
```javascript
IdentifoSingUpActivity.openActivity(this)
```
Also if you want to use identity providers like Facebook, Apple, Google and so on you need to override dedicated resources in your string.xml file.
```javascript
<string name="identifo_facebook_app_id" translatable="false">app_id</string>
<string name="identifo_facebook_protocol_scheme" translatable="false">protocol_schema</string>
```
