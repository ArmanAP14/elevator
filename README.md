شبیه‌ساز سیستم آسانسور با جاوا

یک شبیه‌ساز چندریسمانی (Multithreaded) از سیستم مدیریت آسانسور برای یک ساختمان چندطبقه که با زبان Java و با تمرکز بر مفاهیم شیءگرایی (OOP)، همزمانی (Concurrency)، مدیریت صف‌ها و الگوهای طراحی (Design Patterns) پیاده‌سازی شده است.

در این شبیه‌سازی، مسافران با نقش‌ها و نیازهای مختلف به‌صورت همزمان وارد ساختمان می‌شوند، برای انجام یک Task به طبقه‌ی مشخصی می‌روند، از آسانسور مناسب استفاده می‌کنند و پس از انجام کار به طبقه‌ی همکف بازمی‌گردند. آسانسورها نیز به‌صورت مستقل در Threadهای جداگانه فعالیت کرده و ممکن است در طول شبیه‌سازی دچار خرابی شوند و توسط تعمیرکاران تعمیر شوند.

⸻

معرفی پروژه

این پروژه یک سیستم شبیه‌سازی آسانسور برای یک ساختمان ۸ طبقه است که در آن چندین نوع آسانسور با ظرفیت و کاربرد متفاوت وجود دارد:

* آسانسور عمومی (Public)
* آسانسور ویژه (VIP)
* آسانسور حمل بار (Freight)

هر مسافر دارای ویژگی‌هایی مانند سن، وزن، نقش و Task مشخص است. بر اساس نقش و وزن مسافر، آسانسور مناسب برای او انتخاب می‌شود.

سیستم همچنین شامل یک مکانیزم Fairness برای تعیین اولویت مسافران در صف، خرابی تصادفی آسانسورها، سیستم تعمیر توسط تعمیرکاران و ثبت گزارش نهایی اجرای شبیه‌سازی است.

⸻

ساختار و معماری پروژه

پروژه به چند Package اصلی تقسیم شده است که هرکدام مسئولیت مشخصی دارند:

src/
├── Simulation.java
│
├── controller/
│   └── ElevatorController.java
│
├── model/
│   ├── Elevator.java
│   ├── ElevatorType.java
│   ├── PublicElevator.java
│   ├── VipElevator.java
│   └── FreightElevator.java
│
├── passenger/
│   ├── Passenger.java
│   ├── PassengerRole.java
│   ├── Undergraduate.java
│   ├── Professor.java
│   ├── ViceDean.java
│   ├── Porter.java
│   └── Repairman.java
│
├── task/
│   ├── Task.java
│   └── Priority.java
│
├── floor/
│   ├── Floor.java
│   ├── FloorManager.java
│   └── ElevatorQueue.java
│
├── fairness/
│   ├── FairnessStrategy.java
│   └── PassengerComparator.java
│
└── util/
    ├── PassengerFactory.java
    └── Logger.java

