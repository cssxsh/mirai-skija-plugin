package xyz.cssxsh.skija

import io.github.humbleui.skija.*

public object FontStyles {
    private val manager: FontMgr = FontMgr.getDefault()

    /**
     * 系统默认字体列表
     */
    public fun families(): Map<String, FontStyleSet> = manager.makeFamilies()

    /**
     * 宋体
     */
    public val SimSun: FontStyleSet get() = manager.matchFamily("SimSun")

    /**
     * 新宋体
     */
    public val NSimSun: FontStyleSet get() = manager.matchFamily("NSimSun")

    /**
     * 黑体
     */
    public val SimHei: FontStyleSet get() = manager.matchFamily("SimHei")

    /**
     * 仿宋
     */
    public val FangSong: FontStyleSet get() = manager.matchFamily("FangSong")

    /**
     * 楷体
     */
    public val KaiTi: FontStyleSet get() = manager.matchFamily("KaiTi")

    /**
     * 隶书
     */
    public val LiSu: FontStyleSet get() = manager.matchFamily("LiSu")

    /**
     * 幼圆
     */
    public val YouYuan: FontStyleSet get() = manager.matchFamily("YouYuan")

    /**
     * Arial
     */
    public val Arial: FontStyleSet get() = manager.matchFamily("Arial")

    /**
     * Helvetica
      */
    public val Helvetica: FontStyleSet get() = manager.matchFamily("Helvetica")
}