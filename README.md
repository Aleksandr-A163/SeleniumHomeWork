
# 🧪 Автотесты с использованием Selenoid, Docker-Compose, GGR и GGR-UI

Этот проект содержит набор автоматизированных UI‑тестов, работающих через распределённый Selenium‑кластер на базе **GGR** (Grid Router) и **GGR‑UI**.  
Архитектура позволяет масштабировать тестовые запуски, распределять нагрузку между несколькими Selenoid‑нодами и удобно мониторить состояние кластера.

---

# 📦 Структура проекта

```
SeleniumHomeWork/
├─ docker/
│  ├─ docker-compose.yml          # GGR, GGR-UI, Selenoid-ноды
│  ├─ ggr/
│  │   ├─ quota.xml               # Лимиты пользователей и браузеров
│  │   ├─ router.json             # Маршрутизация на Selenoid-ноды
│  │   └─ users.htpasswd          # Логины/пароли для GGR
│  ├─ ggr-ui/
│  │   └─ config.json             # Настройки UI панели
│  ├─ logs/
│  └─ video/
│
├─ src/
│  ├─ main/java/
│  │  ├─ driver/                  # WebDriverProvider c поддержкой GGR
│  │  ├─ pages/
│  │  ├─ components/
│  │  ├─ utils/
│  │  └─ di/
│  └─ test/java/ui_tests/
│
└─ build.gradle
```

---

# 🏗 Архитектура GGR

GGR — это высокопроизводительный маршрутизатор Selenium‑запросов, размещаемый перед Selenoid‑нодами.

```
          ┌─────────────┐
          │   Тесты     │
          └──────┬──────┘
                 │ WebDriver HTTP
                 ▼
        ┌──────────────────┐
        │       GGR        │
        └───┬──────────────┘
            │ router.json
 ┌──────────┼───────────┬───────────┐
 ▼          ▼           ▼
Selenoid1  Selenoid2   Selenoid3
```

### GGR выполняет:
- распределение нагрузки по нодам  
- учёт лимитов пользователей (quota.xml)  
- контроль количества сессий  
- маршрутизацию к свободной ноде  
- работу с несколькими браузерами  

---

# 📄 Пример `router.json`

```json
{
  "browser": {
    "chrome": [
      { "name": "selenoid_1", "host": "selenoid1:4444" },
      { "name": "selenoid_2", "host": "selenoid2:4444" }
    ]
  }
}
```

---

# 📄 Пример `quota.xml`

```xml
<qa>
  <user name="default">
    <limits>
      <limit name="chrome" max="5"/>
      <limit name="firefox" max="3"/>
    </limits>
  </user>
</qa>
```

---

# 📄 Пример `docker-compose.yml`

```yaml
version: '3.7'

services:

  ggr:
    image: aerokube/ggr:latest
    volumes:
      - ./ggr:/etc/ggr
    ports:
      - "4444:4444"

  ggr-ui:
    image: aerokube/ggr-ui:latest
    ports:
      - "8888:8888"
    volumes:
      - ./ggr-ui:/etc/ggr-ui

  selenoid1:
    image: aerokube/selenoid:latest
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
      - ./browsers:/etc/selenoid/browsers
      - ./logs:/opt/selenoid/logs
      - ./video:/opt/selenoid/video
    environment:
      - TZ=Europe/Moscow

  selenoid2:
    image: aerokube/selenoid:latest
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
      - ./browsers:/etc/selenoid/browsers
      - ./logs:/opt/selenoid/logs
      - ./video:/opt/selenoid/video
```

---

# 🚀 Запуск окружения

```bash
cd docker
docker-compose up -d
```

После старта:

| Компонент | URL |
|----------|------|
| GGR endpoint | http://localhost:4444/wd/hub |
| GGR-UI panel | http://localhost:8888 |

---

# 🧪 Запуск тестов через GGR

### Chrome (десктоп)
```bash
./gradlew clean test -DrunMode=ggr -Dbrowser=chrome
```

### Chrome Mobile
```bash
./gradlew clean test -DrunMode=ggr -Dbrowser=chromeMobile
```

---

# 🧠 Конфигурация WebDriver (GGR)

### Пример SelenoidConfig.java

```java
caps.setCapability("browserName", browser);
caps.setCapability("enableVNC", true);
caps.setCapability("enableVideo", true);

URL ggrUrl = new URL("http://localhost:4444/wd/hub");
return new RemoteWebDriver(ggrUrl, caps);
```

---

# ⚙️ Параллельный запуск

```groovy
test {
    useJUnitPlatform()
    maxParallelForks = Runtime.runtime.availableProcessors().intdiv(2)
}
```

---

# 📂 Логи и видео

После завершения тестов:

```
docker/logs/
docker/video/
```

---

# 🎯 Итог

В проект добавлена поддержка:

- GGR как центрального Selenium‑роутера  
- GGR-UI панели  
- нескольких Selenoid‑нод  
- распределения нагрузки  
- корректного описания браузеров, квот и роутинга  

---

# 📧 Обратная связь

**Автор:** Aleksandr Anosov  
**GitHub:** https://github.com/Aleksandr-A163
