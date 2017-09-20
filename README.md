AutoViewModelFactory
=========================
[![](https://jitpack.io/v/kakai248/AutoViewModelFactory.svg)](https://jitpack.io/#kakai248/AutoViewModelFactory)

This library generates the factory for the Architecture Components ViewModel. To be used with dagger2.

Why do you need this?
------
If you are using Google's ViewModels and dagger2, you should have the problem of not being able to
inject the ViewModel while using scoped parameters in its constructor that are provided by the Activity/Fragment/whatever.
This happens because the ViewModel has a higher scope than the Activity or the Fragment.

You have two options (that I know of):
 - You pass the parameters after the constructor. But you also have to manage if the ViewModel was already initialized.
 - Or you have a factory for each ViewModel. But you will need to create the factory manually. Unnecessary boilerplate.

This library creates the factory for you, only using an annotation on the ViewModel.

Installation
------
This library requires Java 8 to run the annotation processor.

Check the latest version in the badge above.

```groovy
repositories {
    maven { url "https://jitpack.io" }
}
```

```groovy
compile "com.github.kakai248.AutoViewModelFactory:annotations:${LATEST_VERSION}"
annotationProcessor "com.github.kakai248.AutoViewModelFactory:processor:${LATEST_VERSION}"
```

Usage
-------
Annotate your ViewModel with `@AutoViewModelFactory`. Don't annotate the constructor with `@Inject` as you will no longer inject the ViewModel.
You will inject the factory instead, and this one is created by dagger with the default scope (new instance each time).

```java
@AutoViewModelFactory
public class MainViewModel extends ViewModel {
   public MainViewModel() {
   }
}
```

On your activity/fragment/whatever:
```java
public class MainActivity extends DaggerAppCompatActivity {

    @Inject
    MainViewModelFactory viewModelFactory;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
    }
}
```

Example
------
See the included sample app.

Disclaimer
------
The annotation processor recreates the ViewModel package to be able to generate code while referencing it. If Google changes this, the processor may stop working.

License
-------

    Copyright 2017 Ricardo Carrapiço

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.