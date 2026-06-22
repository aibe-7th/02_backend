# 실습 221-1. Java 객체지향 프로그래밍 기초

본 실습은 Java 객체지향 프로그래밍의 핵심 개념과 JVM의 메모리 작동 원리를 코드를 통해 직접 검증하고 이해하기 위한 자료입니다.
총 5개의 단계별 실습 예제로 구성되어 있으며, 2시간 동안 순차적으로 진행합니다.

## 실습 환경 및 실행 방법
- JDK 17 이상 권장
- 터미널에서 컴파일 및 실행:
  ```bash
  # 컴파일 (221-1 폴더에서 실행 시)
  javac *.java
  
  # 개별 클래스 실행
  java Ex01_MemoryAndScope
  java Ex02_ConstructorAndThis
  java Ex03_ParameterPassing
  java Ex04_StaticConstraint
  java Ex05_InnerClassMemoryLeak
  ```

---

## 단계별 실습 주제

### 1단계: JVM 메모리 영역 & 변수 스코프 (`Ex01_MemoryAndScope.java`)
- **목표**: JVM 3대 메모리 영역(Method Area, Heap, Stack)의 구조와 멤버 변수 vs 지역 변수의 Lifecycle 이해
- **실습 내용**:
  - 멤버 변수(인스턴스 변수)의 자동 초기화 값 확인
  - 지역 변수 미초기화 시 컴파일 오류 직접 관찰 및 해결
  - 메서드 호출 시 스택 프레임(Stack Frame)이 생성 및 소멸하는 흐름 이해

### 2단계: 생성자 오버로딩 & `this` 체이닝 (`Ex02_ConstructorAndThis.java`)
- **목표**: 객체 초기화 과정, 기본 생성자(Default Constructor)의 주입 규칙, `this` 및 `this()`의 올바른 활용
- **실습 내용**:
  - 커스텀 생성자 선언 시 컴파일러가 기본 생성자를 제공하지 않아 발생하는 에러 해결
  - `this`를 통한 멤버 변수와 매개변수의 식별
  - `this()`를 첫 라인에 호출하여 생성자 간 코드 중복을 제거하고 초기화를 위임하는 흐름 파악

### 3단계: Call by Value 검증 (`Ex03_ParameterPassing.java`)
- **목표**: Java의 Parameter Passing 방식이 100% Call by Value(값에 의한 전달)로만 동작함을 검증
- **실습 내용**:
  - 기본 타입(Primitive Type) 전달 시 복사된 값의 제어 한계 확인
  - 참조 타입(Reference Type) 전달 시 힙 영역 객체 참조값 복사의 의미 이해
  - 메서드 내부에서 매개변수 자체에 새 객체를 할당할 때 호출부 변수가 유지되는 원리 분석

### 4단계: Static 제약 조건 (`Ex04_StaticConstraint.java`)
- **목표**: `static` 키워드(클래스 멤버)의 메모리상 위치(Method Area)와 사용 시 제약 사항 이해
- **실습 내용**:
  - `static` 변수를 활용한 전역적인 인스턴스 개수 누적
  - `static` 메서드 안에서 인스턴스 멤버(변수, 메서드) 및 `this`에 직접 접근할 때 발생하는 컴파일 에러 원인 및 대안 분석

### 5단계: 내부 클래스와 메모리 누수 (`Ex05_InnerClassMemoryLeak.java`)
- **목표**: 비정적 내부 클래스(Non-static Inner Class)의 바깥 클래스 참조(Hidden Reference) 문제 및 메모리 누수 방지
- **실습 내용**:
  - 비정적 내부 클래스의 인스턴스가 바깥 인스턴스에 대한 참조를 유지하여 GC 대상에서 제외되는 현상 분석
  - 외부 클래스 멤버를 참조하지 않는 내부 클래스는 `static`으로 선언하여 메모리를 안전하게 관리하는 실무 권장 패턴 구현
