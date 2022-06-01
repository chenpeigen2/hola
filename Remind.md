### 对于jdk9+模块化的支持情况
> 不支持 ， 因为 kotlin和java混合编译，打开模块化导致java找不到kotlin文件。如想支持需要查阅gradle的资料去改造（`试了一上午失败了 f**k `）