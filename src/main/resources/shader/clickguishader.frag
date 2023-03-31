/*
 * Original shader from: https://www.shadertoy.com/view/XtcfDM
 */

#ifdef GL_ES
precision mediump float;
#endif


#define COL_RED 1.0
#define COL_GRN .0
#define COL_BLU 1.0

// glslsandbox uniforms
uniform float time;
uniform vec2 resolution;
uniform sampler2D texture;

// shadertoy globals
float iTime = 0.0;
vec3  iResolution = vec3(0.0);

// --------[ Original ShaderToy begins here ]---------- //
float N21(vec2 p) {
	p = fract(p * vec2(2.15, 8.3));
    p += dot(p, p + 2.5);
    return fract(p.x * p.y);
}

vec2 N22(vec2 p) {
	float n = N21(p);
    return vec2(n, N21(p + n));
}

vec2 getPos(vec2 id, vec2 offset) {
	vec2 n = N22(id + offset);
    float x = cos(iTime * n.x);
    float y = sin(iTime * n.y);
    return vec2(x, y) * 0.4 + offset;
}

float distanceToLine(vec2 p, vec2 a, vec2 b) {
	vec2 pa = p - a;
    vec2 ba = b - a;
    float t = clamp(dot(pa, ba) / dot(ba, ba), 0., 1.);
    return length(pa - t * ba);
}

float getLine(vec2 p, vec2 a, vec2 b) {
	float distance = distanceToLine(p, a, b);
    float dx = 15./iResolution.y;
    return smoothstep(dx, 0., distance) * smoothstep(1.2, 0.3, length(a - b));
}

float layer(vec2 st) {
    float m = 0.;
    vec2 gv = fract(st) - 0.5;
    vec2 id = floor(st);
    // m = gv.x > 0.48 || gv.y > 0.48 ? 1. : 0.;
    vec2 pointPos = getPos(id, vec2(0., 0.));
    m += smoothstep(0.05, 0.03, length(gv - pointPos));
    
    float dx=15./iResolution.y;
    // m += smoothstep(-dx,0., abs(gv.x)-.5);
    // m += smoothstep(-dx,0., abs(gv.y)-.5);
    // m += smoothstep(dx, 0., length(gv - pointPos)-0.03);
    
    vec2 p[9];
    p[0] = getPos(id, vec2(-1., -1.));
    p[1] = getPos(id, vec2(-1.,  0.));
    p[2] = getPos(id, vec2(-1.,  1.));
    p[3] = getPos(id, vec2( 0., -1.));
    p[4] = getPos(id, vec2( 0.,  0.));
    p[5] = getPos(id, vec2( 0.,  1.));
    p[6] = getPos(id, vec2( 1., -1.));
    p[7] = getPos(id, vec2( 1.,  0.));
    p[8] = getPos(id, vec2( 1.,  1.));
    
    for (int j = 0; j <=8 ; j++) {
    	m += getLine(gv, p[4], p[j]);
        vec2 temp = (gv - p[j]) * 100.;
        m += 1./dot(temp, temp) * (sin(10. * iTime + fract(p[j].x) * 20.) * 0.5 + 0.5);
        
    }
    
    m += getLine(gv, p[1], p[3]);
    m += getLine(gv, p[1], p[5]);
    m += getLine(gv, p[3], p[7]);
    m += getLine(gv, p[5], p[7]);
    
    // m += smoothstep(0.05, 0.04, length(st - vec2(0., 0.)));
    return m;
}

void mainImage( out vec3 fragColor, in vec2 fragCoord )
{
    vec2 uv = (fragCoord - 0.5 * iResolution.xy) / iResolution.y;
    
    float m = 0.;
    
    float theta = iTime * 0.1;
    mat2 rot = mat2(cos(theta), -sin(theta), sin(theta), cos(theta));
    vec2 gradient = uv;
    uv = rot * uv;
    
    for (float i = 0.; i < 1.0 ; i += 0.25) {
    	float depth = fract(i + iTime * 0.1);
        m += layer(uv * mix(10., 0.5, depth) + i * 20.) * smoothstep(0., 0.2, depth) * smoothstep(1., 0.8, depth);
    }
    
    //vec3 baseColor = sin(vec3(3.45, 6.56, 8.78) * iTime * 0.2) * 0.5 + 0.5;
    vec3 baseColor = sin(vec3(1.0, 1.0, 1.0) * iTime * 0.2) * 0.5 + 1.;
    
    vec3 col = (m - gradient.y) * baseColor;
    // Output to screen
    fragColor = vec3(col.r*COL_RED,col.g*COL_GRN,col.b*COL_BLU);
}
// --------[ Original ShaderToy ends here ]---------- //

float glowShader() {
    float radius = 2.5;
    float quality = 1.0;
    float divider = 158.0;
    float maxSample = 10.0;
    vec2 texelSize = vec2(1.0 / resolution.x * (radius * quality), 1.0 / resolution.y * (radius * quality));
    float alpha = 0;

    for (float x = -radius; x < radius; x++) {
        for (float y = -radius; y < radius; y++) {
            vec4 currentColor = texture2D(texture, gl_TexCoord[0].xy + vec2(texelSize.x * x, texelSize.y * y));

            if (currentColor.a != 0)
            alpha += divider > 0 ? max(0.0, (maxSample - distance(vec2(x, y), vec2(0))) / divider) : 1;
        }
    }

    return alpha;
}

float calcLuma(vec3 color) {
    return 0.299*color.r + 0.587*color.g + 0.114*color.b;
}

void main() {
    vec4 centerCol = texture2D(texture, gl_TexCoord[0].xy);
    iTime = time;
    iResolution = vec3(resolution, 0.0);
    vec3 result;

    mainImage(result, gl_FragCoord.xy);

    float alpha = 0;
    if (centerCol.a != 0) {
        alpha = calcLuma(result)*1.5;
    } else {
        alpha = glowShader();
    }

    gl_FragColor = vec4(result, alpha);
}