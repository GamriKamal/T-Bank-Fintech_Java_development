
# 15.2 Домашнее задание

## Цель:
В этом задании вам предстоит провести тестирование производительности двух популярных брокеров сообщений — RabbitMQ и Kafka — с использованием JMH (Java Microbenchmarking Harness). Вам нужно будет настроить различные конфигурации продюсеров и консюмеров, измерить их производительность в различных сценариях и провести анализ полученных данных.

## Тестирование производительности RabbitMQ и Kafka с использованием JMH.
В этом отчете я сравнил производительность Kafka и RabbitMQ при различных сценариях загрузки.

## Конфигурации для тестирования: 
- Вам нужно протестировать следующие конфигурации продюсеров и консьюмеров для каждой системы:
  - 1 продюсер и 1 консюмер (Simple)
  - 3 продюсера и 1 консюмер (Load balancing)
  - 1 продюсер и 3 консюмера (Multiple consumers)
  - 3 продюсера и 3 консюмера (Load balancing + multiple consumers)
  - 10 продюсеров и 10 консюмеров (Stress test)
---

## Бенчмарк результаты

| **Тест**                                                              | **Производительность (ops/s)** | **Задержка (мс)** | **Время обработки (мс)** | **Репликация (мс)** | **Подтверждение доставки (мс)** | **Комментарий**                                    |
|-----------------------------------------------------------------------|--------------------------------|--------------------|---------------------------|--------------------|---------------------------------|----------------------------------------------------|
| Kafka 1 продюсер и 1 консюмер (Simple)                                | ≈ 0.001                        | 1000.93           | 1184.74                   | 1184.74            | —                               | Зависит от конфигурации.                          |
| RabbitMQ 1 продюсер и 1 консюмер (Simple)                             | 0.349                          | 2.87              | 19279                     | —                  | 1927.066                        | Высокая производительность.                       |
| Kafka 3 продюсера и 1 консюмер (Load balancing)                       | 0.999                          | 1001              | 118                       | 1614.56            | —                               | Ограниченная производительность.                  |
| RabbitMQ 3 продюсера и 1 консюмер (Load balancing)                    | 178.063                        | 6                 | 2.8                       | —                  | 1291.34                         | Оптимально для конкурирующих консюмеров.          |
| Kafka 1 продюсер и 3 консюмера (Multiple consumers)                   | ≈ 0.333                        | 3002              | 3449.82                   | 3449.82            | —                               | Высокие задержки, требует настройки.              |
| RabbitMQ 1 продюсер и 3 консюмера (Multiple consumers)                | 116.709                        | 5.8               | 318                       | —                  | 3186.46                         | Превосходная эффективность.                       |
| Kafka 3 продюсера и 3 консюмера (Load balancing + multiple consumers) | ≈ 0.333                        | 3002              | 3448.29                   | 1067.05            | —                               | Высокие задержки, требует настройки.              |
| RabbitMQ 3 продюсера и 3 консюмера (Load balancing + multiple consumers) | 116.709                        | 5.8               | 318                       | —                  | 4909.33                         | Высокая эффективность.                            |
| Стресс-тест Kafka                                                     | ≈ 0.0001                       | 10008             | 3449.82                   | 4002,82            | —                               | Проблемы масштабируемости под нагрузкой.          |
| Стресс-тест RabbitMQ                                                  | 0.035                          | 28.56             | 3184.202                  | —                  | 2070.89                         | Справляется с нагрузкой с умеренными задержками.   |

---

## Вывод основываясь на бенчмарках
### Kafka
- **Сильные стороны**: Высокая масштабируемость, поддерживает большие объемы данных, отлично подходит для аналитических систем.
- **Слабые стороны**: Высокая задержка, требует точной настройки для обеспечения оптимальной производительности.

### RabbitMQ
- **Сильные стороны**: Низкая задержка, высокая производительность в системах реального времени, простота интеграции.
- **Слабые стороны**: Ограниченная масштабируемость, проблемы с большими наборами данных.
---

## В каких случаях лучше использовать брокер сообщений?
1. **RabbitMQ**: Лучше всего подходит для систем реального времени, где важна низкая задержка.
2. **Kafka**: Идеально подходит для крупномасштабных систем с большими объемами данных и аналитическими нагрузками.
---

