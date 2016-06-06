# AndroidPopupController
popupWindow便捷封装

## Import
[JitPack](https://jitpack.io/)

Add it in your project's build.gradle at the end of repositories:

```gradle
repositories {
  // ...
  maven { url "https://jitpack.io" }
}
```

Step 2. Add the dependency in the form

```gradle
dependencies {
  compile 'com.github.vilyever:AndroidPopupController:2.3.1'
}
```

## Usage
```java

// 初始化
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ViewControllerManager.initialize(this);
    }
}

PopupController popupController = new PopupController(MainActivity.this, R.layout.test_view);
popupController.setPopupBackgroundColor(Color.BLUE);
popupController.setEdgePadding(8, 8, 8, 8);
popupController.setEdgeRoundedRadius(5);

// then call this to popup
popupController.popupFromView(findViewById(R.id.titleLabel), PopupDirection.Top, true);
```

## License
[Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)

