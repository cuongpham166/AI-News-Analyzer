export function getColorCode(colorClass: string) {
  const colorHex = getComputedStyle(document.documentElement)
    .getPropertyValue(colorClass)
    .trim();
  return colorHex;
}
