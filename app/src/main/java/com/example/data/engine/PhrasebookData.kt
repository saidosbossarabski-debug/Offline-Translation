package com.example.data.engine

data class PhraseItem(
    val id: String,
    val category: String,
    val textRu: String,
    val textEn: String,
    val iconEmoji: String
)

object PhrasebookData {
    val CATEGORIES = listOf(
        "Все",
        "Приветствия",
        "Путешествия",
        "Отель",
        "Кафе и еда",
        "Покупки",
        "Чрезвычайные",
        "Цифры"
    )

    val PHRASES: List<PhraseItem> = listOf(
        // Приветствия
        PhraseItem("p1", "Приветствия", "Здравствуйте", "Good day", "👋"),
        PhraseItem("p2", "Приветствия", "Доброе утро", "Good morning", "🌅"),
        PhraseItem("p3", "Приветствия", "Добрый вечер", "Good evening", "🌆"),
        PhraseItem("p4", "Приветствия", "Спокойной ночи", "Good night", "🌙"),
        PhraseItem("p5", "Приветствия", "До свидания", "Goodbye", "🤝"),
        PhraseItem("p6", "Приветствия", "Спасибо", "Thank you", "🙏"),
        PhraseItem("p7", "Приветствия", "Большое спасибо", "Thank you very much", "💖"),
        PhraseItem("p8", "Приветствия", "Пожалуйста", "You're welcome / Please", "✨"),
        PhraseItem("p9", "Приветствия", "Извините", "Excuse me / Sorry", "🙇"),
        PhraseItem("p10", "Приветствия", "Как вас зовут?", "What is your name?", "💬"),
        PhraseItem("p11", "Приветствия", "Приятно познакомиться", "Nice to meet you", "😊"),
        PhraseItem("p12", "Приветствия", "Как дела?", "How are you?", "😃"),
        PhraseItem("p13", "Приветствия", "Хорошо, спасибо", "Fine, thank you", "👍"),

        // Путешествия
        PhraseItem("p14", "Путешествия", "Где находится отель?", "Where is the hotel?", "🏨"),
        PhraseItem("p15", "Путешествия", "Где туалет?", "Where is the bathroom?", "🚻"),
        PhraseItem("p16", "Путешествия", "Где аэропорт?", "Where is the airport?", "✈️"),
        PhraseItem("p17", "Путешествия", "Где вокзал?", "Where is the railway station?", "🚆"),
        PhraseItem("p18", "Путешествия", "Где я?", "Where am I?", "📍"),
        PhraseItem("p19", "Путешествия", "Прямо", "Straight ahead", "⬆️"),
        PhraseItem("p20", "Путешествия", "Налево", "To the left", "⬅️"),
        PhraseItem("p21", "Путешествия", "Направо", "To the right", "➡️"),
        PhraseItem("p22", "Путешествия", "Остановите здесь, пожалуйста", "Stop here, please", "🚖"),
        PhraseItem("p23", "Путешествия", "Мне нужен билет до центра", "I need a ticket to the center", "🎟️"),

        // Отель
        PhraseItem("p24", "Отель", "У меня забронирован номер", "I have a reservation", "🔑"),
        PhraseItem("p25", "Отель", "Есть ли свободные номера?", "Do you have free rooms?", "🛎️"),
        PhraseItem("p26", "Отель", "Какой пароль от Wi-Fi?", "What is the Wi-Fi password?", "📶"),
        PhraseItem("p27", "Отель", "Во сколько выезд?", "What time is checkout?", "⏰"),
        PhraseItem("p28", "Отель", "Мне нужно полотенце", "I need a towel", "🧼"),

        // Кафе и еда
        PhraseItem("p29", "Кафе и еда", "Счет, пожалуйста", "The bill, please", "🧾"),
        PhraseItem("p30", "Кафе и еда", "Вода", "Water", "💧"),
        PhraseItem("p31", "Кафе и еда", "Чай", "Tea", "🍵"),
        PhraseItem("p32", "Кафе и еда", "Кофе", "Coffee", "☕"),
        PhraseItem("p33", "Кафе и еда", "Хлеб", "Bread", "🥖"),
        PhraseItem("p34", "Кафе и еда", "Очень вкусно", "Very delicious", "😋"),
        PhraseItem("p35", "Кафе и еда", "Меню, пожалуйста", "Menu, please", "📖"),
        PhraseItem("p36", "Кафе и еда", "Я вегетарианец", "I am a vegetarian", "🥗"),

        // Покупки
        PhraseItem("p37", "Покупки", "Сколько это стоит?", "How much does it cost?", "🏷️"),
        PhraseItem("p38", "Покупки", "Это слишком дорого", "This is too expensive", "💰"),
        PhraseItem("p39", "Покупки", "Можно скидку?", "Can I get a discount?", "🔖"),
        PhraseItem("p40", "Покупки", "Можно оплатить картой?", "Can I pay by card?", "💳"),
        PhraseItem("p41", "Покупки", "Я хочу купить это", "I want to buy this", "🛍️"),

        // Чрезвычайные
        PhraseItem("p42", "Чрезвычайные", "Помогите", "Help", "🆘"),
        PhraseItem("p43", "Чрезвычайные", "Вызовите полицию", "Call the police", "👮"),
        PhraseItem("p44", "Чрезвычайные", "Вызовите скорую помощь", "Call an ambulance", "🚑"),
        PhraseItem("p45", "Чрезвычайные", "Мне плохо", "I feel sick", "🤒"),
        PhraseItem("p46", "Чрезвычайные", "Я потерялся", "I am lost", "🧭"),
        PhraseItem("p47", "Чрезвычайные", "Где аптека?", "Where is the pharmacy?", "💊"),

        // Цифры
        PhraseItem("p48", "Цифры", "Один", "One", "1️⃣"),
        PhraseItem("p49", "Цифры", "Два", "Two", "2️⃣"),
        PhraseItem("p50", "Цифры", "Три", "Three", "3️⃣"),
        PhraseItem("p51", "Цифры", "Десять", "Ten", "🔟"),
        PhraseItem("p52", "Цифры", "Сто", "One hundred", "💯")
    )
}
