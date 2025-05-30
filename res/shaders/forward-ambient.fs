#version 120
#include "sampling.glh"

varying vec2 texCoord0;
varying vec3 worldPos0;
varying mat3 tbnMatrix;

uniform vec3 R_ambient;
uniform vec3 C_eyePos;

uniform sampler2D diffuse;
uniform sampler2D dispMap;

uniform float dispMapScale;
uniform float dispMapBias;

void main() {
  vec3 directionToEye = normalize(C_eyePos - worldPos0);
  vec2 texCoords = calcParallaxTexCoords(dispMap, tbnMatrix, directionToEye, texCoord0, dispMapScale, dispMapBias);

  //if(texCoords.x > 1.0 || texCoords.y > 1.0 || texCoords.x < 0.0 || texCoords.y < 0.0)
  //  discard;

  gl_FragColor = texture2D(diffuse, texCoords) * vec4(R_ambient, 1);
}
