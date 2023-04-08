package me.madcat.features.gui.font;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import me.madcat.features.gui.font.CFont;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class CFontRenderer
extends CFont {
    protected final CFont.CharData[] boldChars = new CFont.CharData[256];
    protected final CFont.CharData[] italicChars = new CFont.CharData[256];
    protected final CFont.CharData[] boldItalicChars = new CFont.CharData[256];
    private final int[] colorCode = new int[32];
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;

    public CFontRenderer(Font font, boolean bl, boolean bl2) {
        super(font, bl, bl2);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public CFontRenderer(CFont.CustomFont customFont, boolean bl, boolean bl2) {
        super(customFont, bl, bl2);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public void drawStringWithShadow(String string, double d, double d2, int n) {
        float f = this.drawString(string, d + 1.0, d2 + 1.0, n, true);
        this.drawString(string, d, d2, n, false);
    }

    public float drawString(String string, float f, float f2, int n) {
        return this.drawString(string, f, f2, n, false);
    }

    public float drawCenteredString(String string, float f, float f2, int n) {
        return this.drawString(string, f - (float)(this.getStringWidth(string) / 2), f2, n);
    }

    public float drawCenteredStringWithShadow(String string, float f, float f2, int n) {
        float f3 = this.drawString(string, (double)(f - (float)(this.getStringWidth(string) / 2)) + 1.0, (double)f2 + 1.0, n, true);
        return this.drawString(string, f - (float)(this.getStringWidth(string) / 2), f2, n);
    }

    public float drawString(String string, double d, double d2, int n, boolean bl) {
        d -= 1.0;
        if (string == null) {
            return 0.0f;
        }
        if (n == 0x20FFFFFF) {
            n = 0xFFFFFF;
        }
        if ((n & 0xFC000000) == 0) {
            n |= 0xFF000000;
        }
        if (bl) {
            n = (n & 0xFCFCFC) >> 2 | n & 0xFF000000;
        }
        CFont.CharData[] charDataArray = this.charData;
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = false;
        boolean bl6 = true;
        d *= 2.0;
        d2 = (d2 - 3.0) * 2.0;
        if (bl6) {
            GL11.glPushMatrix();
            GL11.glShadeModel(7425);
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.blendFunc(770, 771);
            GL11.glEnable(2848);
            GlStateManager.color((float)(n >> 16 & 0xFF) / 255.0f, (float)(n >> 8 & 0xFF) / 255.0f, (float)(n & 0xFF) / 255.0f, f);
            int n2 = string.length();
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(this.tex.getGlTextureId());
            GL11.glBindTexture(3553, this.tex.getGlTextureId());
            for (int i = 0; i < n2; ++i) {
                char c = string.charAt(i);
                if (c == '\u00A7' && i < n2) {
                    int n3 = 21;
                    try {
                        n3 = "0123456789abcdefklmnor".indexOf(string.charAt(i + 1));
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    if (n3 < 16) {
                        bl2 = false;
                        bl3 = false;
                        bl5 = false;
                        bl4 = false;
                        GlStateManager.bindTexture(this.tex.getGlTextureId());
                        charDataArray = this.charData;
                        if (n3 < 0 || n3 > 15) {
                            n3 = 15;
                        }
                        if (bl) {
                            n3 += 16;
                        }
                        int n4 = this.colorCode[n3];
                        GlStateManager.color((float)(n4 >> 16 & 0xFF) / 255.0f, (float)(n4 >> 8 & 0xFF) / 255.0f, (float)(n4 & 0xFF) / 255.0f, f);
                    } else if (n3 == 17) {
                        bl2 = true;
                        if (bl3) {
                            if (this.texItalicBold != null) {
                                GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                                charDataArray = this.boldItalicChars;
                            }
                        } else if (this.texBold != null) {
                            GlStateManager.bindTexture(this.texBold.getGlTextureId());
                            charDataArray = this.boldChars;
                        }
                    } else if (n3 == 18) {
                        bl4 = true;
                    } else if (n3 == 19) {
                        bl5 = true;
                    } else if (n3 == 20) {
                        bl3 = true;
                        if (bl2) {
                            if (this.texItalicBold != null) {
                                GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                                charDataArray = this.boldItalicChars;
                            }
                        } else if (this.texBold != null) {
                            GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                            charDataArray = this.italicChars;
                        }
                    } else if (n3 == 21) {
                        bl2 = false;
                        bl3 = false;
                        bl5 = false;
                        bl4 = false;
                        GlStateManager.color((float)(n >> 16 & 0xFF) / 255.0f, (float)(n >> 8 & 0xFF) / 255.0f, (float)(n & 0xFF) / 255.0f, f);
                        GlStateManager.bindTexture(this.tex.getGlTextureId());
                        charDataArray = this.charData;
                    }
                    ++i;
                    continue;
                }
                if (c >= charDataArray.length || c < '\u0000') continue;
                GL11.glBegin(4);
                this.drawChar(charDataArray, c, (float)d, (float)d2);
                GL11.glEnd();
                if (bl4) {
                    this.drawLine(d, d2 + (double)(charDataArray[c].height / 2), d + (double)charDataArray[c].width - 8.0, d2 + (double)(charDataArray[c].height / 2));
                }
                if (bl5) {
                    this.drawLine(d, d2 + (double)charDataArray[c].height - 2.0, d + (double)charDataArray[c].width - 8.0, d2 + (double)charDataArray[c].height - 2.0);
                }
                int n5 = charDataArray[c].width - 8;
                this.getClass();
                d += n5;
            }
            GlStateManager.disableBlend();
            GL11.glScalef(2.0f, 2.0f, 2.0f);
            GL11.glShadeModel(7424);
            GL11.glDisable(2848);
            GL11.glHint(3155, 4352);
            GL11.glPopMatrix();
        }
        return (float)d / 2.0f;
    }

    @Override
    public int getStringWidth(String string) {
        if (string == null) {
            return 0;
        }
        int n = 0;
        CFont.CharData[] charDataArray = this.charData;
        boolean bl = false;
        boolean bl2 = false;
        int n2 = string.length();
        for (int i = 0; i < n2; ++i) {
            char c = string.charAt(i);
            if (c == '\u00A7' && i < n2) {
                int n3 = "0123456789abcdefklmnor".indexOf(c);
                if (n3 < 16) {
                    bl = false;
                    bl2 = false;
                } else if (n3 == 17) {
                    bl = true;
                    charDataArray = bl2 ? this.boldItalicChars : this.boldChars;
                } else if (n3 == 20) {
                    bl2 = true;
                    charDataArray = bl ? this.boldItalicChars : this.italicChars;
                } else if (n3 == 21) {
                    bl = false;
                    bl2 = false;
                    charDataArray = this.charData;
                }
                ++i;
                continue;
            }
            if (c >= charDataArray.length || c < '\u0000') continue;
            int n4 = charDataArray[c].width - 8;
            this.getClass();
            n += n4;
        }
        return n / 2;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setAntiAlias(boolean bl) {
        super.setAntiAlias(bl);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setFractionalMetrics(boolean bl) {
        super.setFractionalMetrics(bl);
        this.setupBoldItalicIDs();
    }

    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(Font.BOLD), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(Font.ITALIC), this.antiAlias, this.fractionalMetrics, this.italicChars);
        this.texItalicBold = this.setupTexture(this.font.deriveFont(Font.BOLD | Font.ITALIC), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }

    private void drawLine(double d, double d2, double d3, double d4) {
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex2d(d, d2);
        GL11.glVertex2d(d3, d4);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
    }

    public List<String> formatString(String string, double d) {
        ArrayList<String> arrayList = new ArrayList<>();
        String string2 = "";
        char c = '\uffff';
        char[] cArray = string.toCharArray();
        for (int i = 0; i < cArray.length; ++i) {
            StringBuilder stringBuilder;
            char c2 = cArray[i];
            if (c2 == '\u00A7' && i < cArray.length - 1) {
                c = cArray[i + 1];
            }
            if ((double)this.getStringWidth((stringBuilder = new StringBuilder()).append(string2).append(c2).toString()) < d) {
                string2 = string2 + c2;
                continue;
            }
            arrayList.add(string2);
            string2 = "\u00A7" + c + c2;
        }
        if (string2.length() > 0) {
            arrayList.add(string2);
        }
        return arrayList;
    }

    private void setupMinecraftColorcodes() {
        for (int i = 0; i < 32; ++i) {
            int n = (i >> 3 & 1) * 85;
            int n2 = (i >> 2 & 1) * 170 + n;
            int n3 = (i >> 1 & 1) * 170 + n;
            int n4 = (i & 1) * 170 + n;
            if (i == 6) {
                n2 += 85;
            }
            if (i >= 16) {
                n2 /= 4;
                n3 /= 4;
                n4 /= 4;
            }
            this.colorCode[i] = (n2 & 0xFF) << 16 | (n3 & 0xFF) << 8 | n4 & 0xFF;
        }
    }
}

