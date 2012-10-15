/**
 *
 */
package net.escritoriodigital.unicamp.redefor.bean.content;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;

/**
 * @author andre
 *
 */
public class MediaData implements Serializable {

	private static final long serialVersionUID = -824075448620808920L;

	private Integer Width=110;

	private Integer Height=50;

	private Color Background=new Color(190, 214, 248);

	private Color DrawColor=new Color(0,0,0);

	private Font font = new Font("Serif", Font.TRUETYPE_FONT, 30);

	/**
	 * @return the width
	 */
	public Integer getWidth() {
		return Width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(Integer width) {
		Width = width;
	}

	/**
	 * @return the height
	 */
	public Integer getHeight() {
		return Height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(Integer height) {
		Height = height;
	}

	/**
	 * @return the background
	 */
	public Color getBackground() {
		return Background;
	}

	/**
	 * @param background the background to set
	 */
	public void setBackground(Color background) {
		Background = background;
	}

	/**
	 * @return the drawColor
	 */
	public Color getDrawColor() {
		return DrawColor;
	}

	/**
	 * @param drawColor the drawColor to set
	 */
	public void setDrawColor(Color drawColor) {
		DrawColor = drawColor;
	}

	/**
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * @param font the font to set
	 */
	public void setFont(Font font) {
		this.font = font;
	}

}
