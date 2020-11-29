# BiuVideo

## 📄项目简介
通过bilibili接口获取数据，对视频、音频、图片等资源在线观看、收听、缓存等

## 📘开发日志

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