# Aurora Motion — Fabric, Minecraft Java 26.2

Что внутри
- **Анимации (клиент):** `HumanoidModelMixin` — поворот корпуса, наклон на бегу, покачивание, дыхание,
  замах корпусом при ударе. Работает на игроке, зомби, скелетах и на слоях брони.
- **Броня Aurora:** шлем/нагрудник/поножи/ботинки (креатив → вкладка «Бой», крафт: алмазы + аметист).
- **Эффекты:** светящиеся частицы вокруг игрока в броне (полный сет — ярче).

Сборка
1. JDK 25, Minecraft 26.2 + Fabric Loader ≥ 0.19.3 + Fabric API.
2. Gradle wrapper в архиве нет: сгенерируй шаблон на https://fabricmc.net/develop/template (26.2),
   возьми оттуда папку `gradle/`, `gradlew*`, и сверь версии в gradle.properties.
3. `./gradlew build` → jar в `build/libs/`, кидай в `mods/`. Тест: `./gradlew runClient`.

Если не собралось: ID плагина Loom, версии, пакет HumanoidRenderState и имена полей
(walkAnimationPos/Speed, ageInTicks, attackTime) в 26.2 могли поменяться — IDE подсветит.

## Получить jar без установки Java
1. Создай репозиторий на github.com, залей туда содержимое этой папки (включая `.github`).
2. Вкладка Actions → build → дождись зелёной галочки.
3. Внизу страницы запуска, в Artifacts, скачай `aurora-motion-jar`, распакуй и кинь `.jar` в `mods/`
   (рядом нужен Fabric Loader и Fabric API для 26.2).
Не берите файлы с `-sources` в имени.
