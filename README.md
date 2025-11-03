<img width="1854" height="695" alt="image" src="https://github.com/user-attachments/assets/9e9b702b-781e-44b2-908b-da28e23539ca" /># 📂 High-Performance Persistence Architecture for Minecraft

This project demonstrates a robust and scalable architecture for managing player statistics on Minecraft servers (Paper/Spigot), emphasizing **data safety under high concurrency** and **performance optimization (TPS)**.

The core challenge addressed is the **Read-Modify-Write (RMW)** pattern in a multi-threaded environment, ensuring that slow I/O operations do not block the critical server *Main Thread*.

## 🎯 Key Concepts and Applied Patterns

### 1. Asynchronous Programming with `CompletableFuture` (Service Contract)

* All I/O-bound operations in the `StatsService` return a `CompletableFuture`.
* This ensures the Minecraft **tick loop** (the Main Thread) is never blocked while waiting for the database response.
* **Pattern:** Service-Level Asynchrony.

### 2. Layered Architecture (Design Patterns)

The persistence is organized into a Decorator pipeline: `SqlRepository` → `CachedRepository` → `AsyncStatsRepository`.

* **`CachedRepository` (RMW Atomicity):**
    * Implements **Tier-1 In-Memory Caching** using **Caffeine**.
    * Uses the `Map.compute()` method (from Caffeine/Java) within the `update()` to guarantee **RMW Atomicity**. This replaces manual locking (like `ReentrantLock`), resulting in cleaner code that is safe from deadlocks and *Lost Updates* within the cache.

* **`AsyncStatsRepository` (Threading Layer):**
    * This is the **Asynchrony Decorator**. Its sole responsibility is to wrap the synchronous `CachedRepository` calls in a `CompletableFuture`, dispatching them to a dedicated **I/O Thread Pool**. This frees the `StatsService` from threading complexity.
    * **Patterns:** **Decorator Pattern** and **Single Responsibility Principle (SRP)**.

### 3. Event Serialization Under Stress (`processKillEvent`)

* The `processKillEvent` method (within the `StatsService`) resolves the **Race Condition** observed when multiple kill events are fired rapidly.
* It uses `CompletableFuture.allOf()` to parallelize the **1000 writes** of a single event.
* Crucially, it uses a `CompletableFuture` token (`eventQueues`) and `thenCompose()` to **serialize the start** of each new event.
* **Guarantee:** This forces **Event B** to wait for the complete finalization of **Event A** before its operations begin, ensuring the final read (`getPlayerStats`) always returns the correct, total value (Strong Consistency).

---
<img width="1854" height="695" alt="image" src="https://github.com/user-attachments/assets/5bbde641-f074-4a4a-8ea6-99d2abdf792f" />
