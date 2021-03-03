# BiuVideo

## 📄项目简介
通过BiliBili接口获取数据，对视频、音频、图片等资源在线观看、收听、缓存等

## ⚠注意事项
- 由于`V3`版本会用到高德提供的API，故因此需要`请求服务权限标识`，[申请Key](https://lbs.amap.com/dev/key)
- 如果已经有了`Key`，请在`app/src/main/java/com/leon/biuvideo/values/apis`包下，创建一个名为`AmapKey`的Java文件，并将`Key`作为常量进行存放
  - 示例
  ``` java
      package com.leon.biuvideo.values.apis;

      public class AmapKey {
          public static String amapKey = "key";
      }
  ```

## 📘开发日志

### 2020/03/03
- 完善了设置界面天气模块的开启和主页面天气模块的显示

### 2020/03/02
- 加入了天气模块的开关功能
- 加入了定位功能
- 完善了定位功能，`LocationUtil`只需实例化一次，获取位置信息调用`getAddress()`方法
  - 返回值
    - {province(省份), city(城市), district(区县), street(街道)}

### 2020/03/01
- `V2`分支已停止更新，后续更新将在该分支上进行更新
- 合并了测试版本的代码
- **项目整体需要进行大规模清理**
- 对主页的天气功能进行了修改
  - ~~**在移动网络下，获取的公网IP不能进行定位**~~
  - 可使用手机自带的定位功能来获取其位置

###  横线部分内的为测试版本开发日志

------
### 2020/03/01
- 每次启动APP都会获取一次公网IP，并会根据公网IP获取到所在省市信息（不会存在滥用行为，只会在天气预报定位用到这些信息）
- 加入了公网IP定位功能，和更加准确的县/直辖市信息获取
- 由于`SDK21(Android 5.0)`有部分限制，所以将`minSdkVersion`升到了24(Android 7.0)
- 测试版本已停止更新，后续将会在`V3`分支中进行更新

### 2020/02/28
- 在设置页面加入了`选择位置`功能,该功能用于设置天气预报的位置
- 各天气已测试完成

### 2020/02/27
- 天气获取部分已完成
- ~~**各种天气还未进行测试**~~

### 2020/02/26
- 主页面的`名言名句`替换为了`天气预报`
- 天气预报布局已完成，且获取天气数据部分也已完成
- 由于天气种类过多，后面需要对其进行分类

### 2020/02/25
- 加入了推荐页面
- 热门排行移至了主页

### 2020/02/24
- 加入了收藏夹界面
- 加入了多个`Fragment`,用于`ViewPager`中

### 2020/02/23
- 对部分自定义控件进行了修改
- 加入了设置界面
- 修改了`TabLayout`控件指示条的颜色

### 2020/02/21
- 在用户页面加入了下拉放大图片效果
- `TagView`的部分问题还未解决
- 完善了`TagView`和`SimpleTopBar`
- 加入了`CardTitle`自定义控件

### 2020/02/20
- 使用了单Activity的模式
- `发现`页面已基本完成
- 加入了`SimpleTopBar`,该自定义控件可实现顶部栏，后期会对该控件进行优化
- 完善了用户页面，并对自定义控件的代码进行了完善

### 2021/02/19
- 加入了发现
- 加入了搜索界面

------

### 2020/02/23
- 修复了部分BUG
- 由于现在制作新的版本UI，所以会停止更新一段时间
- 本次更新加入了`备份功能`，这是由于本地数据的问题，在下个版本中会加入很多新功能

### 2020/02/17
- 加入了`new_home_layout.xml`，该文件为替换的主页布局
- 由于要对APP的整体主题进行更新，所以暂时不会发布新版本，只提交代码至gitee

### 2020/02/15
- 对dialog的布局文件进行了重命名
- 修复了订阅页面-视频，列表数据出现重复的问题
- 删除了开源许可弹窗，点击开源许可，将会跳转到项目gitee页面的LISCENSE文件页面
- 修改了部分页面的布局

### 2020/02/14
- 删除了`SimpleSnackBar`中带有监听事件参数的make方法
  - 设置监听统一使用`setAction`进行设置
- 删除了`simple_snackbar_with_action_layout.xml`
- 所有的`SnackBar`均已替换成自定义layout的`SnackBar`
- 修改了部分代码
- 更新了`ThanksList`

### 2020/02/13
- 加入了自定义layoutSnackbar，分为不带监听事件和带有监听事件两种：
  - 不带监听事件
    ``` java
        SimpleSnackBar.make(view, "显示文本", SimpleSnackBar.LENGTH_SHORT/LENGTH_LONG).show();
    ```
  ~~- 带有监听事件~~
    ``` java
        SimpleSnackBar.make(view, "显示文本", "被点击文本", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something...
            }
        }, SimpleSnackBar.LENGTH_SHORT).show();
    ```
- 后面会对所有的`SnackBar`进行替换

### 2020/02/10
- 主题切换的核心部分已完成
  - **由于主题切换功能涉及到很多地方，所以需要在后期慢慢完善全部**
  - 使用格式
    ``` java
        // 初始化本地广播接收器
        ThemeColorChangeBroadcastReceiver themeColorChangeBroadcastReceiver = new ThemeColorChangeBroadcastReceiver();
        themeColorChangeBroadcastReceiver.initBroadcast(getApplicationContext());
        themeColorChangeBroadcastReceiver.setChangeThemeColorListener(new ThemeColorChangeBroadcastReceiver.ChangeThemeColorListener() {
            @Override
            public void changThemeColor(int position) {
                // 修改当前布局主题
            }
        });
    ```
- 在设置界面加入了`原图模式`，核心部分还未编写

### 2020/02/06
- 创建了`V2.1`分支，该分支将着重于UI的修改，该分支不会对功能性代码进行大幅度改动
- 加入了主题颜色，并修改了部分颜色命名

### 2020/01/31
- 修复了使用短信或账号密码登录成功后，不会跳转到主界面的问题

### 2020/01/30
- 修改了视频的合并方式
  - 使用了`FFmpeg`第三方Android库进行音视频合并操作，由于so库过大，所以导致APK大小增加了5M左右
  - 由于是公测版本，所以该版本只支持`arm64-v8a`架构
- 修复了下载页面图片不显示的问题
- 修复了部分已知BUG

### 2020/01/28
- 修复了不能下载VIP资源的问题
- 修复了部分列表数据选择错误的问题
- 修改了视频的下载方式
  - 先对视频、音频进行下载
  - 然后在对其合并，合并完成后删除源文件
  - 下载的文件统一存放在`Temp`文件夹中
  - **注意：第一次启动会删除`Temp`中的所有文件**

### 2020/01/26
- 修复了已知的BUG
- 修复了二次搜索背景出现无数据提示的问题

### 2020/01/23
- 修复了从设置界面导入用户，但在关注界面不显示的BUG
- 修改了版本码

### 2021/01/22
- 能用到`多线程`和`懒加载`的地方均已替换完成
- 修改了`favoriteUp`表
  - 在关注界面中导入的数据和从设置界面导入的数据都视为`用户数据`，在使用`清除用户数据`功能时，会将其删除掉
- 修改了`关注`功能的部分代码
- ~~**感觉`SQLiteHelpFactory`有点鸡肋，所以后面会将其清除掉**~~
- 对代码进行了清理
- 修改了版本号
  - `2.5.8-beta`
- 修复了已知的小问题
- 在视频和番剧播放界面加入了查看下载列表功能
- 修改了部分布局文件
- 加入了`缓存选集帮助链接`
  - [https://www.yuque.com/docs/share/0e9613b0-a9e2-4969-b0a4-97432be7b586?# 《关于选集下载的主要问题》]()

### 2021/01/21
- 部分数据的加载方式更换为了`多线程`进行加载，部分`Fragment`采用了懒加载的方式
- 搜索功能也均替换成了多线程加载
  - **由于对多线程不是很了解，所以不清楚线程是否需要进行回收，但在搜索功能中使用了`SimpleThreadPool`中的`cancelTask`方法来取消已使用过的线程**
  - ~~**还有部分功能未替换成多线程的方式加载，且部分Fragment未替换成懒加载的方式**~~

### 2021/01/20
- 修复了已知的BUG

### 2021/01/19
- 所有的`layout`资源已整理完成
- 删除了未引用的`drawable`文件
- 对部分Java代码进行了整理
- 所有的`Toast`均已替换成`Snackbar`
  - 仍由一小部分未替换，且部分显示的还有问题
- 代码整理阶段已基本结束，剩下的就剩排除BUG了

### 2021/01/18
- 对`localOrders`表进行的修改
  - mainId：用于Video的bvid、Bangumi的mediaId、Music的sid、Article的articleId
  - subId：用于Bangumi的seasonId、Music的bvid、article的mid
  - json：用于存放跳转到对应Activity时用到的数据，或进行显示的数据
- `本地订阅`功能已基本完成
- ~~**该项目的整体代码和文件还需要进行大规模清理**~~
- 对部分代码进行了整理
  - ~~**颜色和字符资源还需要进行统一**~~
- `V2`版本已进入最后整理阶段

### 2021/01/17
- 完善了`VideoActivity`、`BangumiActivity`和`ArticleActivity`中的部分代码
  - ~~**删除了`article`表，将由`LocalOrders`代替，涉及到的其他代码还未进行清理**~~
  - ~~**在`LocalOrders`表中加入了cover字段，涉及到代码还未完成修改**~~

### 2021/01/16
- 删除了`videoPlayList`和`musicPlayList`两个表
  - 以上两个表将由`localVideoFolders`和`localOrders`代替
  - 在`VideoActivity`中点亮红心时，会弹出一个选择要添加进的收藏夹弹窗，第一次启动APP会默认添加一个`默认收藏夹`
  - 修改了`MusicActivity`中的部分代码
  - 播放列表更名为`本地订阅`，这样做是为了能够将喜欢的视频、音频及专栏保存在本地，下一个版本会加入同步(已保存的数据将会根据自已的意愿选择是否进行和B站账户数据同步)，**该功能并不是为了同步，而是尽可能的保持离线化**
- 增加了两个SQLite表格
  - `localVideoFolders`
  - `localOrders`
- 该版本的视频、音频、专栏的收藏将采用本地存储的形式，同步版本会在下一个版本中加入

### 2021/01/15
- 修复了`e7d4aa8c`中的第三个问题
- 修改了获取接口数据的流程，大部分的接口数据都需要通过`Cookie`获取，所有获取接口数据的`Parser类`都需要通过构造方法来创建
  - `Cookie`的获取统一通过`ParserUtils-getInterfaceRequestHeader (Context context)`来获取

### 2021/01/14
- 修复了一个很SD的BUG
- 加入了番剧下载功能
- **未解决问题**
  - 媒体资源下载完成后，发送的本地广播未接收成功(只在下载记录的主界面出现该问题，可以不用解决)
  - 部分视频下载不了(具体原因还未知)
  - ~~如果视频下载过，再将其删除，再下载的话，则该视频不会出现在缓存管理页面中~~

### 2021/01/12-13
- 由于个人原因这两天为进行更新

### 2021/01/11
- 修改了番剧详情的查看方式

### 2021/01/10
- 对`BangumiActivity`进行了完善
- **本次提交运行会出现`NPE异常`，我也不知道为啥有毛病还要提交，可能是为了不让`连续提交`断掉吧(￣▽￣)"**

### 2021/01/09
- 加入了`BangumiActivity`
- 由于B站未提供在线播放番剧接口，所以`V2`版本并没有在线播放功能，在下一个版本中会添加该功能

### 2021/01/08
- 番剧搜索功能已基本完成
- 对代码进行了表面整理

### 2021/01/07
- 加入了搜索番剧的相关工具类
- 修复了搜索界面中的条目标题出现HTML代码的情况
- 修改了部分代码

### 2021/01/06
- 无进度

### 2021/01/05
- 加入了用户的退出或登录会影响`OrderFragment`和`HistoryFragment`的数据
- 修改了`LoginActivity`的布局代码

### 2021/01/04
- 修改了`获取/设置`下载失败资源列表的方式
  - 在`MainActivity`中设置下载失败的资源，在`DownloadFailListFragment`中获取下载失败的资源
  - 更改了部分代码
- 修改了`MainActivity`中的代码，如果在已打开`OrderFragment`的情况下，进行登录，会出现订阅数据不更新的情况
- 修复了部分已知BUG

### 2021/01/03
- 更改了`VideoActivity`的代码

### 2021/01/02
- 加入了选集缓存对话框，如果选集个数大于1，则会弹出该对话框，否则只弹出画质选择对话框
- ~~**单个选集情况下的界面还需要进行修改**~~
- 选集选择下载功能已完成
  - 注意：默认的画质为1080P，如果选择的画质为大会员才能下载的画质，且没有登录大会员账号，将默认下载非大会员的最高画质；如果没有已选择的画质，则默认按照能下载的最高画质进行下载
- 本次提交可能会在`VideoActivity`出现闪退
- 明天会对`VideoActivity`进行修改，BUG一个接一个的出现真是太难了

### 2021/01/01
- 本次提交的版本需要在**2GB运行内存**的机器上才能完整运行，否则会在`UserActivity`界面中出现闪退
- 更改了视频/音频的下载方式

### 2020/12/31
- 在`downloadDetailsForMedia`中加入了`resourceMark`字段，该字段为`UNIQUE`，用来标识已下载过的媒体资源
- `resourceMark`字段格式：
  - 视频：`subId` + "-" + `qualityId`
  - 音频：`mainId`

### 2020/12/30
- 加入了`缓存失败列表`，并加入了`重新下载`功能
- 神了！DEBUG不了？？？
- `重新下载`功能已完成，并在`VideoActivity`的清晰度选择中加入了已下载提醒
- **数据获取部分还需要进行优化，否则很容易出现`NetworkOnMainThreadException`**

### 2020/12/29
- `离线缓存`功能已完成
- 修改了部分布局代码

### 2020/12/28
- 修复了`离线缓存`的部分问题
- `离线缓存`功能，已基本完成，通过Broadcast动态加载还未完成
- 修复了打开已下载音频，出现闪退的情况

### 2020/12/27
- 原本想实现`下载任务`界面的，奈何本人太菜，只能弃坑了
- `离线缓存`功能已基本完成，视频的清晰度分别还需要修改
- ~~**资源文件下载完成后的动态加载到`离线缓存`列表的功能还未实现**~~
- ~~**`收藏`功能和在专栏界面点击用户头像，莫名其妙的出现了BUG，还需要进行修改**~~

### 2020/12/26
- 填坑

### 2020/12/25
- 挖坑

### 2020/12/24
- `OrderFragment`中的`VideoListFragment`已完成

### 2020/12/23
- `UserFragments`中的Fragment已全部继承BaseFragment
- 其他的Fragment将会在以后都继承BaseFragment

### 2020/12/15 - 22
- 由于学业原因，这几天暂时没有进行更新

### 2020/12/14
- 加入了`OrderFragment`，可获取用户订阅的内容
  - 使用该功能时，需要确保‘隐私设置’的追番追剧设置为公开，否则需要通过登录账号来获取
- ~~本次提交还有问题未解决，正在查明问题中~~
- `5b7d840d`的提交的问题已解决

### 2020/12/13
- 无进度

### 2020/12/12
- `HistoryFragment`已基本完成，暂没发现BUG
- 由于数据获取的问题，进入历史记录-专栏中某一条目，在`ArticleActivity`中不显示文章标签和专栏发表时间
- **由于采用的是一次性加载所有页面，进入时需要等待，所以数据加载的问题还需要进行优化**
- 注意：
  - 创建历史记录中的Fragment和Adapter是根据`HistoryType`的值创建的
- 观看直播功能还未进行，直播功能会在第三版时添加

### 2020/12/11
- 只加入了`视频观看记录`的查询，代码还需要进行优化

### 2020/12/10
- 添加了`VideoHistoryFragment`，该Fragment的各项功能都未完成
- 明天再说，2077太香了

### 2020/12/09
- 加入了`BaseFragment`，继承该类可更好的创建和使用Fragment
- 修复了`HomeFragment`中`onHiddenChanged()`方法不起作用的情况
- 对主页面中4个Fragment进行了重构
- 加入了获取历史记录的接口
  - 在主页加入了`HistoryFragment`
  - `HistoryFragment`中`ViewPager`的数据展示还未完成

### 2020/12/08
- 加入了`LoginActivity`,该Activity用来用户登录的操作
- **用户登陆、登出部分已基本完成，还未进行优化**
- 对侧边栏进行了更改
  - 取消了`MainActivity`顶部栏右边的选项(还未删除)，移动到了侧边栏中
- **还未将`PreferenceActivity`转换为Fragment**

### 2020/12/07
- V2版本，在`1.1.3-beta`版本的基础上进行更新
- 在该版本完成之前`master`分支将不会进行更新，只会上传到`V2`分支上
- 该版本将会加入以下主要内容:
  - 用户登录、登出
  - 在线观看番剧和缓存
  - 下载界面
- 更改了侧边栏的样式

### 2020/12/06
- 无进度

### 2020/12/05
- 忘记加项目地址了\[😓\]
- GitHub链接现暂不提供，后期可能会进行提供(其实是连接到GitHub实在太慢，懒的折腾了)

### 2020/12/04
- \[修复\]进入未关注的用户界面时出现应用闪退的情况
- 更改了应用版本号
  - 直到第一个发行版发布后，`build.gradle`的versionCode才进行递增
  - 版本更新后需要对`build.gradle`和`version.xml`中的versionCode和versionName进行更改，其两文件的值需要一一对应相同
- 加入了`帮助`链接
- 修复了`MusicActivity`切换歌曲的问题
- 版本号修改为`1.1.2-beta`
- 修复了部分漏洞
- 修复了下载视频时，清晰度错乱的情况
- 解决了由腾讯云检测出的高、中危漏洞
  - 有些漏洞为第三方组件产生的，且都为低级别，所以未进行修复
  - 不会真有人闲的利用这来搞事情吧
  - `AndroidManifest.xml`中的allowBackup属性，现已改为false，后期会根据反馈情况来决定是否开启
- 版本号修改为`1.1.3-beta`

### 2020/12/03
- 在`PreferenceActivity`加入了`关于本APP`Dialog
- V1.0 Dev正式发布
- 本项目第一版本已进入尾声，该版本完成度接近100%
- 该项目源代码已设置为`开源状态`
- 在帮助文档完成后会上架到部分应用平台
- 现帮助文档进度还未达到100%

### 2020/12/02
- 修复了部分BUG
- **由于获取数据都是在主线程完成的，该缺点现阶段不进行更改，但会在以后根据情况进行更改**
- ~~**关注列表的导入顺序和获取本地数据的顺序还未解决**~~
- 在`PreferenceActivity`中加入了`将关注列表的顺序设置为"按照访问量"排序`功能
- `按照访问量`排序该功能已完成
- 修复了`ImportStateDialog`不显示的问题

### 2020/12/01
- 对部分代码进行了更改
- 加入了感谢名单
  - 后续加入只需对`values`包下的`ThanksList`类中三个数组进行追加即可
- 对开源许可弹窗进行了修改

### 2020/11/30
- 清理了`parseDataUtils`包中的部分代码
- 重命名了部分名称
- 在`PreferenceActivity`中加入了`设置Hero`的功能
- `PreferenceActivity`中的`设置Hero`功能已完成
  - 后期添加Hero的话需要在`HeroImages.heroImages`中加入其资源ID
  - 然后在`preference_strings.xml`中的`heroNames`数组中加入其名称即可
  - **资源ID和heroNames的顺序必须保持一致，否则会出现资源顺序错乱的情况**
- 在`activity_user`和`activity_search_result`中使用了`CoordinatorLayout`控件，已实现折叠悬浮效果
- 对logo进行了更改
  - **侧边栏上的icon还未进行更换**

### 2020/11/29
- 添加了`RoundPopupWindow`类，该类用于创建一个带有圆角的popupWindow
- 使用步骤：
  - 创建该类对象，构造参数需要传入Context，和Anchor(锚点，即显示在那一控件的旁边的那个对象)对象
  - 使用`setContentView(int layoutId)`来设置视图内容
  - `setOnClickListener (int viewId, View.OnClickListener onClickListener)`用来设置控件的监听
  - `setLocation(int location)`根据需要进行设置
  - 最后使用create来创建`RoundPopupWindow`
- 更具体的说明可看对应方法的注释
- 现所有的`more`弹窗都已使用`RoundPopupWindow`进行创建
- 添加了无网络提示，不过使用有点麻烦，后期会根据需要进行优化
  - 调用`InternetUtils`中的静态方法`checkNetwork()`
  - false为无网络，true为有网络
  - 更改提示语句的话只需更改`string.xml`中的`network_sign`即可
- 修改了用户界面中四个Fragment中数据获取的方式，需要先获取总数，再获取具体数据
- 未解决问题：
  - **Fragment子类还存在代码冗余，`parseDataUtils`包中的各解析工具类也存在大量代码冗余**
  - **在用户界面中浏览数据的话，如果滑动的速度过快，很容易使应用出现闪退情况**

### 2020/11/28
- 修改了popupWindow的弹出位置
- 添加了`RoundPopupWindow`抽象类，该类可以创建一个带有圆角样式的popupWindow
  - **但显示位置暂时还没有调整完成**

### 2020/11/27
- 对大部分界面布局代码，和Java代码进行了修改
- 舍弃了音乐播放界面中的收听数量、评论数等，只保留了加入播放列表、音乐对应的视频、跳转至原网站和缓存，和顶部的打开播放列表
  - 同时对音乐播放界面的歌曲名和作者名称的显示位置进行了更改
  - 点击作者名称还可以跳转到作者主页面
- 本次修改使用的大量的圆角样式
- ~~**所有菜单弹窗的显示位置还需要进行调整**~~

### 2020/11/26
- 冗余问题由于技术原因，暂清除不了
- 修改了搜索界面的部分代码
- 在`ArticleActivity`中加入了作者头像、名称的控件
- 加入了`metas`接口链接

### 2020/11/25
- 二次搜索无数据问题已基本解决，如果二次搜索的Adapter没有创建的话，则数据还是显示不了
- 更改了VideoResultFragment的部分代码，还未进行过调试
- 搜索功能已全部完善，暂未遇到BUG的出现
  - **搜索部分的Fragment还存在代码冗余，需要进行清理**

### 2020/11/24
- 修改了部分界面，`SearchResultActivity`还存在输入关键字搜索不了的情况
- 修改了SearchResultActivity的部分代码
- 对部分代码进行了更改，二次搜索，fragment数据的加载还存在问题，切换fragment时存在fragment频繁创建的问题
- ~~**淦，执行二次搜索，fragment数据的加载还是存在问题**~~
- 不过，倒是在`SearchResultActivity`中加入了`setOffscreenPageLimit()`方法，解决了切换fragment时不在加载范围内的fragment会被销毁的问题（总感觉使用这个方法会有点不妥的感觉）

### 2020/11/23
- 进行到了`VideoResultFragment`
- 在`VideoResultFragment`中添加了加载更多功能
- **搜索相关的功能已完成，但代码还存在大部分冗余**
- 对`HomeFragment`中的spinner的样式进行了修改

### 2020/11/22
- 对`HomeFragment`进行了修改，将其中的搜索视频&用户更改为了综合搜索，搜索结果有视频、专栏和用户
- 添加了`SearchResultActivity`,该Activity的作用为搜索结果页面
- ~~**`SearchResultActivity`的界面设计还未完成**~~
- 在`SearchResultActivity`中添加了控件初始化代码

### 2020/11/21
- 无进度

### 2020/11/20
- 添加了LICENSE
- 加入了LicenseDialog
- PreferenceActivity的所有功能已完成
  - **暂未对反馈信息进行处理**
- 修改了部分icon的大小
- 修改了部分布局代码
- 删除了无用的icon

### 2020/11/19
- 修改了`AboutDialog`的界面代码
- 在PreferenceActivity中加入了`导入外部数据`功能
- `ImportFollowDialog`已完成

### 2020/11/18
- 更改了MainActivity中popupWindow中的item,加入了PreferenceActivity
- 加入了清除缓存功能
- ~~ImportFollowDialog进行到了一半。。。太困了。。。~~

### 2020/11/17
- 无进度

### 2020/11/16
- BUG还未修复,需要将sids的类型更换为List集合
- UpSongActivity的BUG已修复,所有关于RecyclerView的Adapter的代码已经进行优化
- 更改了`utils`中的部分目录结构

### 2020/11/15
- 所有的Adapter的冗余已基本重写完成
- 注意：
  - 创建Adapter时按照以下的格式创建
  - ``` java
        public class Adapter extends BaseAdapter<MusicPlayList> {
    
            @Override
            public int getLayout(int viewType) {
                return itemID;
            }
            
            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
                //调用holder中对应的方法，具体说明可看代码注释
            }
            
            //其他方法
        }
    ```
  - **`MusicPlayListAdapter`相关的适配还未完成,切换歌曲时`position`会出现未重置成功的情况**

### 2020/11/14
- 对获取接口响应数据的方式进行了更改
  - 获取接口数据的方法
    1. 对请求头(Header)有特殊要求的
	``` java
        public HttpUtils(String url, Headers headers, Map<String, String> params) {
        	this.url = url;
        	this.headers = headers;
        	this.params = params;
        }
	```

    2. 对请求头没有要求的
	``` java
        public HttpUtils(String url, Map<String, String> params) {
        	this.url = url;
        	this.params = params;
        	this.headers = Headers.of(getHeaders());
        }
	```

- 对连接SQLite的方式进行了更改
  - 创建数据连接时必须通过创建`SQLiteHelperFactory`类,构造方法参数只用传入`Context`和对应的Tables,调用`getInstance()`方法来获取对象,**需要进行强制转换**
  - 使用完数据库之后只需在对应的`Destroy`中调用其`close()`方法即可,**使用完数据库必须要调用该方法**
- ~~**各适配器(Adapter)的代码还未进行冗余清除**~~

### 2020/11/13
- 以下两项**暂定**
  - 添加了Logo
  - 更换了侧滑栏头部显示的图片
- 修复了关注用户、收藏视频或歌曲后，关注列表、播放列表的数据没有及时更新的情况
- 修复了第一次进入，关注用户、收藏视频或歌曲后，关注列表、播放列表的数据没有及时更新的情况

### 2020/11/12
- PictureViewer已完成
  - ~~**该控件的进入和退出动画还未添加**~~
- PictureViewer的进入/退出动画已完成
- 更改了UpMasterActivity中的控件
  - ~~**其他的ViewPage顶部的`指示栏`还未进行相同的更改**~~
- 对其他`指示栏`进行了更改，对部分控件添加了`ripple`效果

### 2020/11/11
- 又到了一年一度的光棍节！！！
- 加入了PictureActivity
- 添加了PictureActivity的初始化代码
- 修改了PictureActivity的部分代码
- PictureActivity部分的代码已大部分完成
  - ~~**图片查看器还在着手准备中**~~
- PictureViewer已添加，但还有些许问题需要进行修复

### 2020/11/10
- 主界面的弹窗问题已解决
- 对部分按钮加入的`ripple`效果
- 对大部分控件添加了`ripple`效果
  - ~~**RecyclerView的item的ripple效果尚未添加成功**~~
  - 减少了部分冗余代码
- 修复了musicPlayList的video图标显示的问题
- 修复了videoPlayList的cover圆角显示的问题
- 所有`item`的`ripple`效果都已添加
- 修复了所有数据已加载完，但刷新控件还在开启的情况
- 修复了一部分的问题
- ~~**查看`picture`界面还未添加**~~

### 2020/11/09
- 修改了VideoActivity的样式和视频清晰度选择的方式
- 修复了下载视频时选择清晰度不全或多余的情况
- 修复了关注页面的用户名和用户说明显示的问题
- 修改了VideoActivity中响应体的获取方式
- ~~**主页面"more"中的popupMenu需要进行更改**~~
- 主页面的popupMenu已更改，但就是有点'丑'
- ~~**注意,如果`AndroidManifest`中的MainActivity的Theme使用`more_menu_style`的话，则输入框旁边的`spinner`的样式会失效**~~

### 2020/11/08
- 文章以长图的方式进行保存的功能已完成
- 修复了VideoActivity底部颜色不全的问题
- ~~**VideoActivity的界面还需要进行改进**~~

### 2020/11/07
- 添加了文章Activity
- 在用户界面中增加了文章列表
- 文章列表和文章Activity还未完善

### 2020/11/06
- 网页读取测试

### 2020/11/05
- 无进展

### 2020/11/04
- 添加了针对"专栏"界面进行删除网页元素的JS脚本文件
- ~~**删除专栏div元素未能解决**~~

### 2020/11/03
- 添加了专栏页面Activity，用户页面内还没有加入对应的Fragment
- 网页读取测试

### 2020/11/02
- fragment的重影问题已解决
- 修复了一些小问题，列表中的viewPage没有添加监听事件
- ~~权限的获取还有待改进，重影问题还未解决~~
- 权限获取已完成
- 对代码进行了清理，但还存在代码冗余
  - MediaUtils类中的getHeaders()方法属于冗余部分，该方法待清理
  - ~~**从“收藏”转到“播放列表”后播放列表上面会出现重影**~~
  - 播放列表中Video的图标颜色不一样
  - 不搞了，晚上再搞。。。
- 增加了图片缓存功能，对代码进行了清理
- 每次缓存都会调用FileUtils中的verifyPermissions方法来获取权限，回调方法在每个对应的Activity中

### 2020/11/01
- 无进度

### 2020/10/31
- 在侧滑栏中添加了`播放列表`功能
  - 播放列表中添加了一个两个界面
    - 视频播放列表
    - 音乐播放列表
- ~~**侧边栏还存在fragment重影情况**~~
- ~~**视频列表歌和音乐列表中的“无数据”提示控件未能完美隐藏掉**~~

### 2020/10/30
- 音乐切换功能已完成，播放列表Fragment待添加
- 可能在其他机型上面还存在问题，如果出现歌曲已播放完，动画和控制按钮可能会保持不变，可通过修改`MusicService`中的differenceRange变量的值来解决问题
- 修复了第一次进入，播放完一整首歌，再退出进入时出现歌曲不能播放的问题

### 2020/10/29
- 播放列表的更新已解决
- 歌曲的切换和切换为播放列表里面的歌曲已完成
- ~~**切换其他歌曲后，再点击添加到“播放列表”，再切换到其他歌曲后“红心”依然存在**~~
- ~~**歌曲切换后的播放按钮的状态和动画在实体机上面还存在问题，动画为暂停状态，播放按钮也为暂停状态**~~

### 2020/10/28
- 添加了服务，但服务的注册一直搞不定！！！
- 服务已搞定，可进行播放音乐，但离开播放音乐界面，音乐也就停了；音乐的保存还尚有问题没有解决
- ~~**音乐播放已没问题，添加到播放列表后，播放列表不能及时更新暂未解决**~~
- 播放上一曲、下一曲和显示歌词还未动工

### 2020/10/27
- Favorite的表控件需要更改为RecyclerView（到现在为止所有的表控件都为RecyclerView）
- 修改的话明天再说，ListView太垃圾了
- 修改了Favorite的表控件
- 添加了播放列表页面，对Dialog添加了动画
- 修改了一些控件的圆角样式
- 添加了服务，但服务的注册一直搞不定！！！
- 音频界面及数据的获取已完成，FavoriteFragment的监听事件还未修改完
- "终于"想起来加入了README了:)

### 2020/10/26
- 缓存视频功能已完成，所有的hero都已更换为Vector类型文件

### 2020/10/25
- 使用侧滑栏切换Fragment的重叠已修复，增加了bvid和mid的获取方式，增加了网络状态的获取，对一些细节进行了优化
- 缓存视频功能已完成，所有的hero都已更换为Vector类型文件

### 2020/10/24
- 无进度

### 2020/10/23
- 上拉加载更多数据功能已完成，加入了无数据提示页面，但代码冗余度较高，三个模块的fragment和适配器部分代码需要减少冗余

### 2020/10/22
- 基本信息、视频、音频和相簿的解析都已完成，相簿的解析还有待完善，这IDE有毒。。。
- IDE是真的有毒！！！文件不可写了！！！

### 2020/10/21
- 增加了用户搜索功能，可查看所有已发布的视频、音频、相簿，以上三个功能接口响应体的解析还未进行
- 获取UP主所有视频接口的响应体解析已完成

### 2020/10/20
- 打开方式更改为了Fragment的形式
- 在ToolBar中添加了选项菜单

### 2020/10/19
- 修改了选集列表的控件为RecyclerView，适配器能添加成功，但item显示不了
- 添加了切换选集功能

### 2020/10/18
- 对项目进行了初始化

## 🙎‍♂️参与人员
- 个人开发