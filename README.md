# 🧪 Автотесты для OTUS.ru с использованием Selenoid и Docker

Этот проект содержит набор автоматизированных тестов для сайта [otus.ru](https://otus.ru), реализованных с использованием **Java 17**, **Selenium WebDriver 4+**, **JUnit 5**, **Google Guice (DI)** и **Gradle**.  
Проект поддерживает запуск как локально, так и через **Selenoid** с возможностью эмуляции мобильных устройств и автоматическим разворачиванием окружения через **Ansible**.

---

## 📦 Структура проекта

```
SeleniumHomeWork/
├─ docker/
│  ├─ docker-compose.yml      # Конфигурация контейнеров Selenoid и Selenoid UI
│  ├─ browsers.json           # Поддерживаемые браузеры и версии
│  ├─ deploy_selenoid.yml     # Ansible playbook для автоматического деплоя
│  ├─ logs/                   # Логи Selenoid
│  └─ video/                  # Видео выполнения тестов
│
├─ src/
│  ├─ main/java/
│  │  ├─ components/          # Web-компоненты (CourseListComponent, HeaderMenuComponent и др.)
│  │  ├─ pages/               # Page Object классы (BasePage, MainPage, CourseCatalogPage, CoursePage)
│  │  ├─ driver/              # Фабрика WebDriver (WebDriverProvider, BrowserFactory, BrowserType, SelenoidConfig)
│  │  ├─ di/                  # Guice-модуль и расширение (TestModule, GuiceExtension)
│  │  └─ utils/               # Утилиты (HighlightingListener)
│  └─ test/java/
│     └─ ui_tests/            # Тестовые сценарии (CourseSearchTest, CourseDateTest, NavigationMenuTest)
│
├─ build.gradle
└─ README.md
```

---

## 🚀 Быстрый старт

1. **Клонировать репозиторий**
   ```bash
   git clone https://github.com/Aleksandr-A163/SeleniumHomeWork
   cd SeleniumHomeWork
   ```

2. **Запустить тесты (локально)**
   ```bash
   ./gradlew clean test
   ```

3. **Открыть отчёт**
   ```
   build/reports/tests/test/index.html
   ```

---

## ✅ Реализованные фичи

- **Dependency Injection** через Google Guice (`@Inject`)
- **JUnit 5 Extension** (`GuiceExtension`) без BaseTest
- **Подсветка элементов** при взаимодействиях (`HighlightingListener`)
- **Декоратор WebDriver** (`EventFiringDecorator` для логирования)
- **Stream API + Jsoup** — для обработки дат курсов
- **Checkstyle + SpotBugs** для статического анализа
- **Параллельный запуск тестов**
- **Selenoid + Docker Compose + Ansible** для полного CI/CD запуска

---

## Сценарии тестирования

- `CourseSearchTest` — Навигация на случайный курс из фиксированного списка  
- `CourseDateTest` — Поиск курса с самой ранней/поздней датой начала  
- `NavigationMenuTest` — Переход на случайную категорию из меню «Обучение» и проверка URL  

---

# 🚀 Запуск тестов через Selenoid

## 📦 Установка и запуск Selenoid (через Docker Compose)

1. Перейдите в папку `docker/`
   ```bash
   cd docker
   docker-compose up -d
   ```

2. После запуска:
   - Панель управления: [http://localhost:8080](http://localhost:8080)
   - Эндпоинт Selenium: `http://localhost:4444/wd/hub`

3. Проверка статуса браузеров:
   ```bash
   curl http://localhost:4444/status | jq '.browsers'
   ```

Ожидаемый ответ:
```json
{
  "browsers": {
    "chrome": { "122.0": { "image": "selenoid/vnc:chrome_122.0" } },
    "firefox": { "122.0": { "image": "selenoid/vnc:firefox_122.0" } },
    "opera": { "107.0": { "image": "selenoid/vnc:opera_107.0" } }
  }
}
```

---

## ⚙️ Автоматический деплой Selenoid (через Ansible)

Файл: `docker/deploy_selenoid.yml`

Для удалённого деплоя:
```bash
ansible-playbook deploy_selenoid.yml -i hosts
```

Playbook выполняет:
- Установку Docker и Docker Compose (если не установлены)
- Развёртывание `selenoid` и `selenoid-ui`
- Проверку порта `4444`
- Создание каталогов `logs/` и `video/` для логов и видео

---

## 🧪 Запуск тестов

### 🖥️ Десктопный Chrome
```bash
.\gradlew clean test -DrunMode=selenoid -Dbrowser=chrome
```

### 📱 Мобильный Chrome (эмуляция iPhone X)
```bash
.\gradlew clean test -DrunMode=selenoid -Dbrowser=chromeMobile
```

### 💻 Локальный запуск (без Selenoid)
```bash
.\gradlew clean test -DrunMode=local -Dbrowser=chrome
```

---

## ⚙️ Параллельный запуск тестов

В `build.gradle` добавлен параметр:

```groovy
test {
    useJUnitPlatform()
    maxParallelForks = Runtime.runtime.availableProcessors().intdiv(2) ?: 1
}
```

Это позволяет выполнять тесты параллельно (на ~половине доступных CPU ядер).

---

## 🧩 Файлы конфигурации

### 📁 `docker/browsers.json`
```json
{
  "chrome": {
    "default": "122.0",
    "versions": {
      "122.0": { "image": "selenoid/vnc:chrome_122.0", "port": "4444" }
    }
  },
  "firefox": {
    "default": "122.0",
    "versions": {
      "122.0": { "image": "selenoid/vnc:firefox_122.0", "port": "4444" }
    }
  },
  "opera": {
    "default": "107.0",
    "versions": {
      "107.0": { "image": "selenoid/vnc:opera_107.0", "port": "4444" }
    }
  }
}
```

### 📄 `docker/deploy_selenoid.yml`
Ansible playbook для быстрой установки и запуска окружения на сервере.

---

## 🧰 Полезные команды Docker

```bash
docker ps                   # Проверить запущенные контейнеры
docker logs selenoid        # Логи Selenoid
docker logs selenoid-ui     # Логи панели управления
docker-compose down         # Остановить контейнеры
docker network ls           # Проверить сети Docker
```

---

## 🧠 Конфигурация SelenoidConfig.java

Файл `src/main/java/driver/SelenoidConfig.java`  
поддерживает режимы:
- `createDesktopChrome()`
- `createChromeMobileIPhoneX()`

Подключается к:
```
http://host.docker.internal:4444/wd/hub
```

---

## ✅ Проверка успешности

```bash
docker-compose up -d
.\gradlew clean test -DrunMode=selenoid -Dbrowser=chrome
```

Ожидается:
- Selenoid доступен по `http://localhost:8080`
- В панели видны активные сессии
- Тесты проходят успешно (2–3 из 3)
- Видео и логи сохраняются в `/docker/video` и `/docker/logs`

---

## 📧 Обратная связь

**Автор:** Aleksandr Anosov  
**GitHub:** [Aleksandr-A163](https://github.com/Aleksandr-A163)
