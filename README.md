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
Check the current user authentication status as follow:
```javascript
IdentifoAuthentication.fetchAuthState { state: AuthState ->
    when (state) {
        is AuthState.Authentificated -> // user is authentheficated
        else -> // user is deauthentheficated
    }
}
```

## Identifo UI
### Using
If you don't need a complex design, it may be better to use a dedicated interface which should be enough for common cases. First of all add `navigation_graph_identifo.xml` as [nested graph](https://developer.android.com/guide/navigation/navigation-nested-graphs?authuser=1) to your root Navigation Component graph. 
Example:
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

findNavController().navigate(
    R.id.action_demoFragment_to_navigation_graph_login,
    WelcomeLoginFragment.putArguments(loginOptions)
)
```
Where `R.id.action_demoFragment_to_navigation_graph_login` is redirection action id to the Identifo graph.

Also if you want to use identity providers like Facebook, Apple, Google and so on you need to override dedicated resources in your `string.xml` file.
```javascript
<string name="identifo_facebook_app_id" translatable="false">app_id</string>
<string name="identifo_facebook_protocol_scheme" translatable="false">protocol_schema</string>
```
### Styling
Identifo UI uses [Material color style system](https://material.io/design/color/the-color-system.html#color-usage-and-palettes) so you easily can style Identifo UI according to your application requirements.
For example if you want to change color palette you should define follow colors in your main theme:
```javascript
<item name="colorPrimary">@color/purple_200</item>
<item name="colorPrimaryVariant">@color/purple_500</item>
<item name="colorOnPrimary">@color/white</item>
<item name="background">@color/white</item>
<item name="colorOnBackground">@color/black</item>
<item name="colorSurface">@color/gray_400</item>
<item name="colorOnSurface">@color/gray_200</item>
<item name="colorError">@color/red</item>
```
See an extended example [here](https://github.com/MadAppGang/teamGrowth-android).