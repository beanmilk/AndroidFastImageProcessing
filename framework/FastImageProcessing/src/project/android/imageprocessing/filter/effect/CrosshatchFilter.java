package project.android.imageprocessing.filter.effect;

import android.opengl.GLES20;
import project.android.imageprocessing.filter.BasicFilter;

public class CrosshatchFilter extends BasicFilter {
	protected static final String UNIFORM_CROSS_HATCH_SPACING = "u_CrossHatchSpacing";
	protected static final String UNIFORM_LINE_WIDTH = "u_LineWidth";
	
	private int crossHatchSpacingHandle;
	private int lineWidthHandle;
	private float crossHatchSpacing;
	private float lineWidth;
	
	public CrosshatchFilter(float crossHatchSpacing, float lineWidth) {
		this.crossHatchSpacing = crossHatchSpacing;
		this.lineWidth = lineWidth;
	}
	
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		crossHatchSpacingHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_CROSS_HATCH_SPACING);
		lineWidthHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_LINE_WIDTH);
	}
	
	@Override
	protected void passShaderValues() {
		super.passShaderValues();
		GLES20.glUniform1f(crossHatchSpacingHandle, crossHatchSpacing);
		GLES20.glUniform1f(lineWidthHandle, lineWidth);
	}
	
	@Override
	protected String getFragmentShader() {
		return 
				"precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_CROSS_HATCH_SPACING+";\n"	
				+"uniform float "+UNIFORM_LINE_WIDTH+";\n"
				+"const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n"
				
		  		+"void main(){\n"
		  		+"  highp float luminance = dot(texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+").rgb, W);\n"
		  		+"  lowp vec4 colorToDisplay = vec4(1.0, 1.0, 1.0, 1.0);\n"
		  		+"  if (luminance < 1.00) {\n"
				+"    	if (mod("+VARYING_TEXCOORD+".x + "+VARYING_TEXCOORD+".y, "+UNIFORM_CROSS_HATCH_SPACING+") <= "+UNIFORM_LINE_WIDTH+") {\n"
				+"      	colorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n"
		  	    +"	    }\n"
		  	    +"	}\n"
		  	    +"	if (luminance < 0.75) {\n"
		  	    +"    	if (mod("+VARYING_TEXCOORD+".x - "+VARYING_TEXCOORD+".y, "+UNIFORM_CROSS_HATCH_SPACING+") <= "+UNIFORM_LINE_WIDTH+") {\n"
		  	    +"       	colorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n"
		  	    +"     	}\n"
		  	    +" 	}\n"
		  	    +"	if (luminance < 0.50) {\n"
		  	    +"    	if (mod("+VARYING_TEXCOORD+".x + "+VARYING_TEXCOORD+".y - ("+UNIFORM_CROSS_HATCH_SPACING+" / 2.0), "+UNIFORM_CROSS_HATCH_SPACING+") <= "+UNIFORM_LINE_WIDTH+") {\n"
		  	    +"        	colorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n"
		  	    +"    	}\n"
		  	    +"	}\n"
		  	    +"	if (luminance < 0.3) {\n"
		  	    +"		if (mod("+VARYING_TEXCOORD+".x - "+VARYING_TEXCOORD+".y - ("+UNIFORM_CROSS_HATCH_SPACING+" / 2.0), "+UNIFORM_CROSS_HATCH_SPACING+") <= "+UNIFORM_LINE_WIDTH+") {\n"
		  	    +"     		colorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n"
		  	    +"		}\n"
		  	    +"	}\n"
		  	    +"  gl_FragColor = colorToDisplay;\n"
		  		+"}\n";	
	}
}