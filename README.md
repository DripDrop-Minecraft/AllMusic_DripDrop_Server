# AllMusic for DripDrop

## GPL-3.0 License
>AllMusic for DripDrop, a plugin that allows players listening to music in Minecraft server DripDrop.
> 
>Copyright (C) 2022  Git-a-Live
> 
>This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
> 
> This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
>
> You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 
 
## 重要声明

> 本项目仅基于源项目已有的**歌曲资源链接解析业务做二次开发**，在运行时所接收和解析的任何链接，
> **均由玩家通过游戏内聊天栏自行输入**。玩家自行提供的任何链接，都将被视为<font color=red>大众可在互联网上自行搜索获得的、允许任意访问的公开内容</font>，
> 本项目<font color=red>**不承诺**</font>对它们的合法性进行任何审查或作出任何保证。因链接合法性产生的相关纠纷，本项目亦不承担任何责任。
> 此外，本项目无意为任何使用者提供存储或下载任意资源的功能，亦不会主动开发或鼓励他人开发类似功能。因使用者私自存储链接资源并对外提供下载服务所引发的相关纠纷，均与本项目无关。

## 简介

一个专为 DripDrop 服务器定制的全服点歌插件，基于源项目 [AllMusic_P](https://github.com/HeartAge/AllMusic_P) 修改。

插件的安装使用方法与源项目基本一致，不需要特殊适配，详见 https://github.com/HeartAge/AllMusic_P#readme 。

> 注意：打包时务必使用build.gradle里面的`showJar`命令，编译fat-jar，否则外部依赖库不能合并进jar包，在使用时可能就会抛出ClassNotFoundException。

## 修改要点

+ **向非管理员身份玩家开放/music url指令**

> 源项目中只有管理员身份玩家可以调用/music url指令，让服务器解析网易云以外的歌曲资源链接。从理论上说，只要提供任意有效链接，服务端插件就能解析出来。
> 但是按照作者的说法，随意向非管理员身份玩家开放调用/music url指令会为服务器带来不可预测的风险。
>
> 因为一旦有玩家提供恶意链接让服务器解析，就有可能引发严重后果，包括但不限于服务器被入侵、数据遭到破坏等。
> 为避免发生这种情况，本项目在修改时对传入的链接进行了检测筛选，确保玩家传入的链接仅来源于特定网站，并且只能是歌曲资源链接。

+ **统一指令形式**

> 源项目支持以`/music <网易云歌曲数字ID>`和`/music <网易云歌曲链接>`的形式来解析播放网易云音乐曲库中的资源，
> 而非网易云歌曲只能通过管理员调用`/music url <歌曲资源链接>`的方式让服务器解析并播放。
>  
> 本项目进行修改后，所有玩家都能够直接以`/music <歌曲资源链接>`的形式直接让服务器解析播放可用的非网易云歌曲资源链接。
> 但是正如上一条要点所说的，为确保服务器的安全运行，目前仅对特定网站的歌曲资源链接进行检测适配，如果有需要可以调整适配条件，以接入其他平台。
