package xyz.cssxsh.skija

import io.github.humbleui.skija.*
import io.github.humbleui.skija.paragraph.*
import java.util.*
import kotlin.collections.*
import kotlin.jvm.*

public object FontStyles {
    private val default: FontMgr = FontMgr.getDefault()

    public fun services(): ServiceLoader<TypefaceFontProvider> {
        return ServiceLoader.load(TypefaceFontProvider::class.java, this::class.java.classLoader)
    }

    /**
     * 系统默认字体列表
     */
    public fun families(): Set<String> {
        val names: MutableSet<String> = HashSet()
        repeat(default.familiesCount) { index -> names.add(default.getFamilyName(index)) }
        for (manager in services()) {
            repeat(manager.familiesCount) { index -> names.add(manager.getFamilyName(index)) }
        }

        return names
    }

    /**
     * 获取指定的 [Typeface]
     */
    @Throws(NoSuchElementException::class)
    public fun matchFamilyStyle(familyName: String, style: FontStyle): Typeface {
        return default.matchFamilyStyle(familyName, style)
            ?: services().firstNotNullOfOrNull { provider -> provider.matchFamilyStyle(familyName, style) }
            ?: throw NoSuchElementException("$familyName - $style")
    }

    /**
     * 获取指定的 [Typeface]
     */
    @Throws(NoSuchElementException::class)
    public fun matchFamiliesStyle(families: Array<String>, style: FontStyle): Typeface {
        return default.matchFamiliesStyle(families, style)
            ?: services().firstNotNullOfOrNull { provider -> provider.matchFamiliesStyle(families, style) }
            ?: throw NoSuchElementException("${families.asList()} - $style")
    }

    /**
     * 宋体
     */
    public fun matchSimSun(style: FontStyle): Typeface = matchFamilyStyle("SimSun", style)

    /**
     * 新宋体
     */
    public fun matchNSimSun(style: FontStyle): Typeface = matchFamilyStyle("NSimSun", style)

    /**
     * 黑体
     */
    public fun matchSimHei(style: FontStyle): Typeface = matchFamilyStyle("SimHei", style)

    /**
     * 仿宋
     */
    public fun matchFangSong(style: FontStyle): Typeface = matchFamilyStyle("FangSong", style)

    /**
     * 楷体
     */
    public fun matchKaiTi(style: FontStyle): Typeface = matchFamilyStyle("KaiTi", style)

    /**
     * 隶书
     */
    public fun matchLiSu(style: FontStyle): Typeface = matchFamilyStyle("LiSu", style)

    /**
     * 幼圆
     */
    public fun matchYouYuan(style: FontStyle): Typeface = matchFamilyStyle("YouYuan", style)

    /**
     * Arial
     */
    public fun matchArial(style: FontStyle): Typeface = matchFamilyStyle("Arial", style)

    /**
     * Helvetica
      */
    public fun matchHelvetica(style: FontStyle): Typeface = matchFamilyStyle("Helvetica", style)
}