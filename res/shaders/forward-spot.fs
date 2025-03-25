#version 120
#include "lighting.fsh"

uniform SpotLight R_spotLight;

vec4 calcLightEffect(vec3 normal, vec3 worldPos) {
  return calcSpotLight(R_spotLight, normal, worldPos);
}

#include "lightingMain.fsh"
