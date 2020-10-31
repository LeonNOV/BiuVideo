# BiuVideo

## 📄项目简介
通过bilibili接口获取数据，对视频、音频、图片等资源在线观看、收听、缓存等

## 📘开发日志

### 2020/10/30
- 在侧滑栏中添加了`播放列表`功能
  - > 播放列表中添加了一个两个界面
    > 视频播放列表
    > 音乐播放列表
- **侧边栏还存在fragment重影情况**
- **视频列表歌和音乐列表中的“无数据”提示控件未能完美隐藏掉**


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
- Favorite的表控件已更改为RecyclerView
- 修改了Favorite的表控件
- 添加了播放列表界面
- 添加了播放列表页面，对Dialog添加了动画
- 修改了一些控件的圆角样式
- 添加了服务，但服务的注册一直搞不定！！！

### 2020/10/27
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