# 潜行模式 (Stealth Mode) 实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**目标：** 实现"潜行模式"功能，勾选后TTS听书时不显示书籍私有信息

**架构：** 在 AppSP 中添加布尔设置项，创建 StealthMode 工具类提供占位图标和通用文本，在各显示位置检测设置并切换显示内容

**技术栈：** Android (Java), SharedPreferences, TTS, Widget, ShortcutManager

---

## 文件结构

| 操作 | 文件路径 |
|------|---------|
| 新增 | `app/src/main/java/com/foobnix/tts/StealthMode.java` |
| 修改 | `app/src/main/java/com/foobnix/model/AppState.java` |
| 修改 | `app/src/main/java/com/foobnix/pdf/info/SP.java` |
| 修改 | `app/src/main/java/com/foobnix/ui2/fragment/PrefFragment2.java` |
| 修改 | `app/src/main/java/com/foobnix/tts/TTSNotification.java` |
| 修改 | `app/src/main/java/com/foobnix/pdf/info/widget/TTSWidget.java` |
| 修改 | `app/src/main/java/com/foobnix/pdf/info/widget/RecentBooksWidget.java` |
| 修改 | `app/src/main/java/com/foobnix/pdf/info/widget/RecentUpates.java` |
| 修改 | `app/src/main/java/com/foobnix/pdf/search/activity/HorizontalViewActivity.java` |
| 修改 | `app/src/main/java/com/foobnix/pdf/search/activity/VerticalViewActivity.java` |
| 修改 | `app/src/main/res/values/strings.xml` |
| 修改 | `app/src/main/res/values-zh-rCN/strings.xml` |
| 修改 | `app/src/main/res/values-zh-rTW/strings.xml` |

---

## 实现步骤

### Task 1: 添加字符串资源

**文件:** `app/src/main/res/values/strings.xml`
**文件:** `app/src/main/res/values-zh-rCN/strings.xml`
**文件:** `app/src/main/res/values-zh-rTW/strings.xml`

- [ ] **Step 1: 在 strings.xml 添加字符串**

```xml
<string name="stealth_mode">潜行模式</string>
<string name="stealth_mode_desc">隐藏通知栏和小部件中的书籍信息</string>
<string name="stealth_reading">正在朗读</string>
<string name="stealth_book">书籍</string>
```

- [ ] **Step 2: 在 zh-rCN strings.xml 添加对应中文**

```xml
<string name="stealth_mode">潜行模式</string>
<string name="stealth_mode_desc">隐藏通知栏和小部件中的书籍信息</string>
<string name="stealth_reading">正在朗读</string>
<string name="stealth_book">书籍</string>
```

- [ ] **Step 3: 在 zh-rTW strings.xml 添加对应繁体中文**

```xml
<string name="stealth_mode">潛行模式</string>
<string name="stealth_mode_desc">隱藏通知欄和小部件中的書籍信息</string>
<string name="stealth_reading">正在朗讀</string>
<string name="stealth_book">書籍</string>
```

- [ ] **Step 4: Commit**

```bash
git add app/src/main/res/values/strings.xml app/src/main/res/values-zh-rCN/strings.xml app/src/main/res/values-zh-rTW/strings.xml
git commit -m "feat(stealth-mode): add string resources for stealth mode"
```

---

### Task 2: 创建 StealthMode 工具类

**文件:** `app/src/main/java/com/foobnix/tts/StealthMode.java` (新建)

- [ ] **Step 1: 创建 StealthMode.java**

```java
package com.foobnix.tts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.foobnix.pdf.info.R;

public class StealthMode {

    public static Bitmap getPlaceholderCover(Context context) {
        int size = (int) context.getResources().getDimension(android.R.dimen.app_icon_size);
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, size, size, paint);
        return bitmap;
    }

    public static String getReadingLabel(Context context) {
        return context.getString(R.string.stealth_reading);
    }

    public static String getBookLabel(Context context) {
        return context.getString(R.string.stealth_book);
    }

    public static String getRecentBookShortLabel(Context context) {
        return context.getString(R.string.stealth_book);
    }

    public static String getRecentBookLongLabel(Context context) {
        return context.getString(R.string.stealth_book);
    }
}
```

- [ ] **Step 2: Commit**

```bash
git add app/src/main/java/com/foobnix/tts/StealthMode.java
git commit -m "feat(stealth-mode): create StealthMode utility class"
```

---

### Task 3: 添加设置存储

**文件:** `app/src/main/java/com/foobnix/pdf/info/SP.java`

- [ ] **Step 1: 添加 stealthMode 字段和 getter/setter**

在 `SP` 类中添加:
```java
public static final String STEALTH_MODE = "stealthMode";

public boolean isStealthMode() {
    return pref.getBoolean(STEALTH_MODE, false);
}

public void setStealthMode(boolean value) {
    pref.edit().putBoolean(STEALTH_MODE, value).apply();
}
```

- [ ] **Step 2: Commit**

```bash
git add app/src/main/java/com/foobnix/pdf/info/SP.java
git commit -m "feat(stealth-mode): add stealth mode setting to SP"
```

---

### Task 4: 在设置页面添加勾选项

**文件:** `app/src/main/java/com/foobnix/ui2/fragment/PrefFragment2.java`

- [ ] **Step 1: 找到添加勾选项的位置**

在 PrefFragment2.java 中搜索 `stealth_mode` 或找到通知/TTS 相关设置区域

- [ ] **Step 2: 添加勾选项代码**

在适当位置添加:
```java
addCheckBox(appName, AppSP.get().isStealthMode(), (v) -> {
    AppSP.get().setStealthMode(((CheckBox) v).isChecked());
}, R.string.stealth_mode, R.string.stealth_mode_desc);
```

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/foobnix/ui2/fragment/PrefFragment2.java
git commit -m "feat(stealth-mode): add stealth mode checkbox to settings"
```

---

### Task 5: 修改 TTSNotification

**文件:** `app/src/main/java/com/foobnix/tts/TTSNotification.java`

- [ ] **Step 1: 找到 show() 方法中设置书名和封面的位置**

约在 line 170-210 区域

- [ ] **Step 2: 修改 show() 方法**

在设置书名和封面之前添加检测:
```java
if (AppSP.get().isStealthMode()) {
    holder.bookInfo.setText(StealthMode.getReadingLabel(context));
    holder.ttsIcon.setImageBitmap(StealthMode.getPlaceholderCover(context));
} else {
    // 原有的书名设置逻辑
    String fileMetaBookName = TxtUtils.getFileMetaBookName(fileMeta);
    ...
}
```

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/foobnix/tts/TTSNotification.java
git commit -m "feat(stealth-mode): hide book info in TTS notification"
```

---

### Task 6: 修改 TTSWidget

**文件:** `app/src/main/java/com/foobnix/pdf/info/widget/TTSWidget.java`

- [ ] **Step 1: 找到 onUpdate() 方法中设置书名和封面的位置**

约在 line 60-120 区域

- [ ] **Step 2: 修改 onUpdate() 方法**

在设置书名和封面之前添加检测:
```java
if (AppSP.get().isStealthMode()) {
    views.setTextViewText(R.id.bookInfo, StealthMode.getReadingLabel(context));
    views.setImageViewBitmap(R.id.ttsIcon, StealthMode.getPlaceholderCover(context));
} else {
    // 原有的书名设置逻辑
}
```

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/foobnix/pdf/info/widget/TTSWidget.java
git commit -m "feat(stealth-mode): hide book info in TTSWidget"
```

---

### Task 7: 修改 RecentBooksWidget

**文件:** `app/src/main/java/com/foobnix/pdf/info/widget/RecentBooksWidget.java`

- [ ] **Step 1: 找到 loadAndUpdateWidget() 方法中设置书名的位置**

约在 line 150-200 区域

- [ ] **Step 2: 修改书名设置逻辑**

```java
String name = ...;
if (AppSP.get().isStealthMode()) {
    name = StealthMode.getBookLabel(context);
}
```

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/foobnix/pdf/info/widget/RecentBooksWidget.java
git commit -m "feat(stealth-mode): hide book info in RecentBooksWidget"
```

---

### Task 8: 修改 RecentUpates (快捷菜单)

**文件:** `app/src/main/java/com/foobnix/pdf/info/widget/RecentUpates.java`

- [ ] **Step 1: 找到 updateAll() 方法中创建 ShortcutInfo 的位置**

约在 line 80-120 区域

- [ ] **Step 2: 修改快捷菜单创建逻辑**

在设置 shortLabel/longLabel/icon 之前添加检测:
```java
if (AppSP.get().isStealthMode()) {
    shortLabel = StealthMode.getRecentBookShortLabel(c);
    longLabel = StealthMode.getRecentBookLongLabel(c);
    icon = Icon.createWithResource(c, R.drawable.ic_launcher);
} else {
    // 原有的逻辑
    shortLabel = recentLast.getTitle();
    longLabel = TxtUtils.getFileMetaBookName(recentLast);
    ...
}
```

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/foobnix/pdf/info/widget/RecentUpates.java
git commit -m "feat(stealth-mode): hide book info in app shortcuts"
```

---

### Task 9: 修改 HorizontalViewActivity

**文件:** `app/src/main/java/com/foobnix/pdf/search/activity/HorizontalViewActivity.java`

- [ ] **Step 1: 找到设置顶部状态栏书名的位置**

约在 line 1136 区域搜索 `book_is_open` 或类似

- [ ] **Step 2: 修改书名透明度**

在设置书名的地方添加:
```java
if (AppSP.get().isStealthMode()) {
    bookTitleTextView.setAlpha(0f);
}
```

- [ ] **Step 3: 找到设置阅读内容透明度的位置**

约在 line 2052 区域

- [ ] **Step 4: 修改内容透明度**

```java
if (AppSP.get().isStealthMode()) {
    contentView.setAlpha(0f);
}
```

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/foobnix/pdf/search/activity/HorizontalViewActivity.java
git commit -m "feat(stealth-mode): hide book title and content in horizontal view"
```

---

### Task 10: 修改 VerticalViewActivity

**文件:** `app/src/main/java/com/foobnix/pdf/search/activity/VerticalViewActivity.java`

- [ ] **Step 1: 找到设置顶部状态栏书名的位置**

搜索类似的书名设置代码

- [ ] **Step 2: 修改书名透明度**

```java
if (AppSP.get().isStealthMode()) {
    bookTitleTextView.setAlpha(0f);
}
```

- [ ] **Step 3: 找到设置阅读内容透明度的位置**

- [ ] **Step 4: 修改内容透明度**

```java
if (AppSP.get().isStealthMode()) {
    contentView.setAlpha(0f);
}
```

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/foobnix/pdf/search/activity/VerticalViewActivity.java
git commit -m "feat(stealth-mode): hide book title and content in vertical view"
```

---

### Task 11: 构建并测试

- [ ] **Step 1: 清理并构建**

```bash
./gradlew clean assembleFdroidDebug
```

- [ ] **Step 2: 安装到模拟器**

```bash
adb uninstall com.foobnix.pro.pdf.reader
adb install app/build/outputs/apk/fdroid/debug/Librera\ Fdroid-9.3.63-uni.apk
```

- [ ] **Step 3: 启动应用并测试**

```bash
adb shell am start -n com.foobnix.pro.pdf.reader/com.foobnix.ui2.MainTabs2
```

- [ ] **Step 4: 验证**

1. 进入设置，勾选"潜行模式"
2. 打开一本书，启动 TTS
3. 检查通知栏是否显示"正在朗读"且无书名封面
4. 添加 TTSWidget 到桌面，检查是否显示占位图标
5. 长按应用图标，检查快捷菜单是否无书名
6. 添加 RecentBooksWidget，检查是否无书名
7. 打开书籍，检查顶部状态栏和内容是否完全透明

- [ ] **Step 5: 最终 Commit**

```bash
git add -A
git commit -m "feat: implement stealth mode feature"
```
