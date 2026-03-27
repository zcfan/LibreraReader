# 潜行模式 (Stealth Mode) 设计文档

## 概述

添加一个"潜行模式"功能，用户勾选后，在 TTS 听书时不暴露任何书籍私有信息，包括通知栏、桌面小组件、快捷菜单以及阅读界面中的书名和内容。

## 功能需求

1. **设置项**：在设置页面添加"潜行模式"勾选项
2. **通知栏 TTS**：隐藏书名、作者、封面，显示"正在朗读"及占位图标
3. **TTSWidget**：隐藏书名、封面，显示占位图标
4. **RecentBooksWidget**：隐藏书名，显示"书籍"等通用文本
5. **快捷菜单**：隐藏书名和封面，使用通用文本和默认图标
6. **阅读界面**：顶部状态栏书名和阅读内容本身通过透明度完全透明处理

## 实现细节

### 1. 设置存储

**文件**: `app/src/main/java/com/foobnix/model/AppState.java`
- 添加 `stealthMode` 布尔字段

**文件**: `app/src/main/java/com/foobnix/pdf/info/SP.java`
- 添加 `stealthMode` 的 getter/setter

**文件**: `app/src/main/java/com/foobnix/ui2/fragment/PrefFragment2.java`
- 添加"潜行模式"勾选项，文本使用 `R.string.stealth_mode`

**文件**: `app/src/main/res/values/strings.xml`
- 添加 `<string name="stealth_mode">潜行模式</string>`

### 2. 占位符图标生成

**新增文件**: `app/src/main/java/com/foobnix/tts/StealthMode.java`
- 方法 `getPlaceholderCover(Context)`: 返回纯色矩形 Bitmap
- 方法 `getStealthLabel(Context, String type)`: 返回通用文本
  - "正在朗读" (notification)
  - "书籍" (widget)
  - "最近阅读" (shortcut short)
  - "最近阅读的书籍" (shortcut long)

### 3. TTS 通知栏修改

**文件**: `app/src/main/java/com/foobnix/tts/TTSNotification.java`

修改 `show()` 方法 (line ~96):
```java
if (AppSP.get().isStealthMode()) {
    // 书名/作者改为通用文本
    holder.bookInfo.setText("正在朗读");
    // 封面改为占位图标
    icon.setImageBitmap(StealthMode.getPlaceholderCover(context));
}
```

### 4. TTSWidget 修改

**文件**: `app/src/main/java/com/foobnix/pdf/info/widget/TTSWidget.java`

修改 `onUpdate()` 方法 (line ~53):
```java
if (AppSP.get().isStealthMode()) {
    // 隐藏书名
    views.setTextViewText(R.id.bookInfo, "正在朗读");
    // 隐藏封面
    views.setImageViewBitmap(R.id.ttsIcon, StealthMode.getPlaceholderCover(context));
}
```

### 5. RecentBooksWidget 修改

**文件**: `app/src/main/java/com/foobnix/pdf/info/widget/RecentBooksWidget.java`

修改 `loadAndUpdateWidget()` 方法 (line ~152):
```java
if (AppSP.get().isStealthMode()) {
    // 显示通用文本替代书名
    name = context.getString(R.string.book);
}
```

### 6. RecentUpates 修改 (快捷菜单)

**文件**: `app/src/main/java/com/foobnix/pdf/info/widget/RecentUpates.java`

修改 `updateAll()` 方法 (line ~42):
```java
if (AppSP.get().isStealthMode()) {
    // 快捷方式使用通用文本
    shortLabel = c.getString(R.string.reading_out_loud);
    longLabel = c.getString(R.string.reading_out_loud);
    // 图标使用默认应用图标
    icon = Icon.createWithResource(c, R.drawable.ic_launcher);
}
```

### 7. 阅读界面修改

**文件**: `app/src/main/java/com/foobnix/pdf/search/activity/HorizontalViewActivity.java`
**文件**: `app/src/main/java/com/foobnix/pdf/search/activity/VerticalViewActivity.java`

修改顶部状态栏书名 (约 line ~1136):
```java
if (AppSP.get().isStealthMode()) {
    // 完全透明
    bookTitleTextView.setAlpha(0f);
}
```

修改阅读内容透明度 (约 line ~2052):
```java
if (AppSP.get().isStealthMode()) {
    // 完全透明
    contentView.setAlpha(0f);
}
```

### 8. 字符串资源

**文件**: `app/src/main/res/values/strings.xml`
```xml
<string name="stealth_mode">潜行模式</string>
<string name="stealth_mode_desc">隐藏通知栏和小部件中的书籍信息</string>
<string name="stealth_reading">正在朗读</string>
<string name="stealth_book">书籍</string>
```

**文件**: `app/src/main/res/values-zh-rCN/strings.xml`
```xml
<string name="stealth_mode">潜行模式</string>
<string name="stealth_mode_desc">隐藏通知栏和小部件中的书籍信息</string>
<string name="stealth_reading">正在朗读</string>
<string name="stealth_book">书籍</string>
```

**文件**: `app/src/main/res/values-zh-rTW/strings.xml`
```xml
<string name="stealth_mode">潛行模式</string>
<string name="stealth_mode_desc">隱藏通知欄和小部件中的書籍信息</string>
<string name="stealth_reading">正在朗讀</string>
<string name="stealth_book">書籍</string>
```

## 测试验证

1. 开启潜行模式，播放 TTS，检查通知栏是否显示"正在朗读"且无书名
2. 添加 TTSWidget 到桌面，检查是否显示占位图标
3. 长按应用图标，检查快捷菜单是否无书名
4. 添加 RecentBooksWidget，检查是否无书名
5. 打开书籍，检查顶部状态栏和内容是否完全透明

## 文件清单

- 新增: `app/src/main/java/com/foobnix/tts/StealthMode.java`
- 修改: `app/src/main/java/com/foobnix/model/AppState.java`
- 修改: `app/src/main/java/com/foobnix/pdf/info/SP.java`
- 修改: `app/src/main/java/com/foobnix/ui2/fragment/PrefFragment2.java`
- 修改: `app/src/main/java/com/foobnix/tts/TTSNotification.java`
- 修改: `app/src/main/java/com/foobnix/pdf/info/widget/TTSWidget.java`
- 修改: `app/src/main/java/com/foobnix/pdf/info/widget/RecentBooksWidget.java`
- 修改: `app/src/main/java/com/foobnix/pdf/info/widget/RecentUpates.java`
- 修改: `app/src/main/java/com/foobnix/pdf/search/activity/HorizontalViewActivity.java`
- 修改: `app/src/main/java/com/foobnix/pdf/search/activity/VerticalViewActivity.java`
- 修改: `app/src/main/res/values/strings.xml`
- 修改: `app/src/main/res/values-zh-rCN/strings.xml`
- 修改: `app/src/main/res/values-zh-rTW/strings.xml`
