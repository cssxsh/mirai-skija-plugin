package xyz.cssxsh.skija.gif


public enum class DisposalMethod(public val flag: Int) {
    UNSPECIFIED(0x00),
    DO_NOT_DISPOSE(0x04),
    RESTORE_TO_BACKGROUND(0x08),
    RESTORE_TO_PREVIOUS(0x0C),
}