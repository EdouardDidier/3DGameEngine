#include "sampling.glh"

float calcShadowAmount(sampler2D shadowMap, vec4 initialShadowMapCoords) {
  vec3 shadowMapCoords = initialShadowMapCoords.xyz / initialShadowMapCoords.w;

  return sampleVarianceShadowMap(shadowMap, shadowMapCoords.xy, shadowMapCoords.z);
}

void main() {
  vec3 directionToEye = normalize(C_eyePos - worldPos0);
  vec2 texCoords = calcParallaxTexCoords(dispMap, tbnMatrix, directionToEye, texCoord0, dispMapScale, dispMapBias);

  //if(texCoords.x > 1.0 || texCoords.y > 1.0 || texCoords.x < 0.0 || texCoords.y < 0.0)
  //  discard;

  vec3 normal = normalize(tbnMatrix * (255.0/128.0 * texture2D(normalMap, texCoords.xy).xyz - 1));

  vec4 lightAmt = calcLightEffect(normal, worldPos0) * calcShadowAmount(R_shadowMap, shadowMapCoords0);
  gl_FragColor = texture2D(diffuse, texCoords.xy) * lightAmt;
}
