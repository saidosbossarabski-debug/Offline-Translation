package com.example.data.engine

data class TranslationResult(
    val translatedText: String,
    val phonetic: String = "",
    val originalText: String,
    val sourceLangCode: String,
    val targetLangCode: String,
    val isExactMatch: Boolean = true,
    val notes: String = ""
)

data class LexiconEntry(
    val ru: String,
    val en: String,
    val zh: String,
    val zhPinyin: String = "",
    val es: String = "",
    val de: String = "",
    val fr: String = "",
    val it: String = "",
    val ja: String = "",
    val jaRomaji: String = "",
    val ko: String = "",
    val koRomaji: String = "",
    val tr: String = "",
    val ar: String = "",
    val arPhonetic: String = "",
    val pt: String = "",
    val hi: String = "",
    val kk: String = "",
    val uz: String = "",
    val uk: String = "",
    val pl: String = "",
    val category: String = "Общее"
)

object OfflineTranslationEngine {

    // Massive multilingual offline lexicon covering everyday speech, travel, vocabulary, and concepts
    val VOCABULARY: List<LexiconEntry> = listOf(
        // Greetings & Basics
        LexiconEntry(
            ru = "Привет", en = "Hello", zh = "你好", zhPinyin = "Nǐ hǎo",
            es = "Hola", de = "Hallo", fr = "Bonjour", it = "Ciao",
            ja = "こんにちは", jaRomaji = "Konnichiwa", ko = "안녕하세요", koRomaji = "Annyeonghaseyo",
            tr = "Merhaba", ar = "مرحبا", arPhonetic = "Marhaban", pt = "Olá",
            hi = "नमस्ते", kk = "Сәлем", uz = "Salom", uk = "Привіт", pl = "Cześć",
            category = "Приветствия"
        ),
        LexiconEntry(
            ru = "Здравствуйте", en = "Good day", zh = "您好", zhPinyin = "Nín hǎo",
            es = "Buenos días", de = "Guten Tag", fr = "Bonjour", it = "Buongiorno",
            ja = "こんにちは", jaRomaji = "Konnichiwa", ko = "안녕하십니까", koRomaji = "Annyeonghasimnikka",
            tr = "İyi günler", ar = "أهلاً وسهلاً", arPhonetic = "Ahlan wa sahlan", pt = "Bom dia",
            hi = "शुभ दिन", kk = "Сәлеметсіз бе", uz = "Assalomu alaykum", uk = "Доброго дня", pl = "Dzień dobry",
            category = "Приветствия"
        ),
        LexiconEntry(
            ru = "Доброе утро", en = "Good morning", zh = "早上好", zhPinyin = "Zǎoshang hǎo",
            es = "Buenos días", de = "Guten Morgen", fr = "Bonjour", it = "Buongiorno",
            ja = "おはようございます", jaRomaji = "Ohayō gozaimasu", ko = "좋은 아침입니다", koRomaji = "Joeun achim-imnida",
            tr = "Günaydın", ar = "صباح الخير", arPhonetic = "Sabah al-khair", pt = "Bom dia",
            hi = "सुप्रभात", kk = "Қайырлы таң", uz = "Xayrli tong", uk = "Доброго ранку", pl = "Dzień dobry",
            category = "Приветствия"
        ),
        LexiconEntry(
            ru = "Добрый вечер", en = "Good evening", zh = "晚上好", zhPinyin = "Wǎnshang hǎo",
            es = "Buenas tardes", de = "Guten Abend", fr = "Bonsoir", it = "Buonasera",
            ja = "こんばんは", jaRomaji = "Konbanwa", ko = "좋은 저녁입니다", koRomaji = "Joeun jeonyeog-imnida",
            tr = "İyi akşamlar", ar = "مساء الخير", arPhonetic = "Masa' al-khair", pt = "Boa noite",
            hi = "शुभ संध्या", kk = "Қайырлы кеш", uz = "Xayrli kech", uk = "Доброго вечора", pl = "Dobry wieczór",
            category = "Приветствия"
        ),
        LexiconEntry(
            ru = "Спокойной ночи", en = "Good night", zh = "晚安", zhPinyin = "Wǎn'ān",
            es = "Buenas noches", de = "Gute Nacht", fr = "Bonne nuit", it = "Buonanotte",
            ja = "おやすみなさい", jaRomaji = "Oyasuminasai", ko = "안녕히 주무세요", koRomaji = "Annyeonghi jumuseyo",
            tr = "İyi geceler", ar = "تصبح على خير", arPhonetic = "Tisbah 'ala khair", pt = "Boa noite",
            hi = "शुभ रात्रि", kk = "Қайырлы түн", uz = "Xayrli tun", uk = "Добраніч", pl = "Dobranoc",
            category = "Приветствия"
        ),
        LexiconEntry(
            ru = "До свидания", en = "Goodbye", zh = "再见", zhPinyin = "Zàijiàn",
            es = "Adiós", de = "Auf Wiedersehen", fr = "Au revoir", it = "Arrivederci",
            ja = "さようなら", jaRomaji = "Sayōnara", ko = "안녕히 가세요", koRomaji = "Annyeonghi gaseyo",
            tr = "Hoşça kal", ar = "مع السلامة", arPhonetic = "Ma'a salama", pt = "Adeus",
            hi = "अलविदा", kk = "Сау болыңыз", uz = "Xayr", uk = "До побачення", pl = "Do widzenia",
            category = "Приветствия"
        ),
        LexiconEntry(
            ru = "Пока", en = "Bye", zh = "拜拜", zhPinyin = "Bàibài",
            es = "Chao", de = "Tschüss", fr = "Salut", it = "Ciao",
            ja = "じゃあね", jaRomaji = "Jā ne", ko = "잘 가", koRomaji = "Jal ga",
            tr = "Görüşürüz", ar = "وداعاً", arPhonetic = "Wada'an", pt = "Tchau",
            hi = "बाय", kk = "Көріскенше", uz = "Ko'rishguncha", uk = "Бувай", pl = "Cześć",
            category = "Приветствия"
        ),
        LexiconEntry(
            ru = "Спасибо", en = "Thank you", zh = "谢谢", zhPinyin = "Xièxie",
            es = "Gracias", de = "Danke", fr = "Merci", it = "Grazie",
            ja = "ありがとうございます", jaRomaji = "Arigatō gozaimasu", ko = "감사합니다", koRomaji = "Gamsahamnida",
            tr = "Teşekkür ederim", ar = "شكراً", arPhonetic = "Shukran", pt = "Obrigado",
            hi = "धन्यवाद", kk = "Рахмет", uz = "Rahmat", uk = "Дякую", pl = "Dziękuję",
            category = "Вежливость"
        ),
        LexiconEntry(
            ru = "Большое спасибо", en = "Thank you very much", zh = "非常感谢", zhPinyin = "Fēicháng gǎnxiè",
            es = "Muchas gracias", de = "Vielen Dank", fr = "Merci beaucoup", it = "Grazie mille",
            ja = "どうもありがとう", jaRomaji = "Dōmo arigatō", ko = "대단히 감사합니다", koRomaji = "Daedanhi gamsahamnida",
            tr = "Çok teşekkürler", ar = "شكراً جزيلاً", arPhonetic = "Shukran jazilan", pt = "Muito obrigado",
            hi = "बहुत बहुत धन्यवाद", kk = "Көп рахмет", uz = "Katta rahmat", uk = "Дуже дякую", pl = "Bardzo dziękuję",
            category = "Вежливость"
        ),
        LexiconEntry(
            ru = "Пожалуйста", en = "Please / You're welcome", zh = "不客气", zhPinyin = "Bù kèqi",
            es = "Por favor / De nada", de = "Bitte", fr = "S'il vous plaît / De rien", it = "Prego",
            ja = "どういたしまして", jaRomaji = "Dōitashimashite", ko = "천만에요", koRomaji = "Cheonman-eyo",
            tr = "Lütfen / Rica ederim", ar = "عفواً", arPhonetic = "Afwan", pt = "Por favor / De nada",
            hi = "कृपया / आपका स्वागत है", kk = "Оқасы жоқ", uz = "Arzimaydi", uk = "Будь ласка", pl = "Proszę",
            category = "Вежливость"
        ),
        LexiconEntry(
            ru = "Извините", en = "Excuse me / Sorry", zh = "不好意思", zhPinyin = "Bù hǎoyìsi",
            es = "Disculpe / Perdón", de = "Entschuldigung", fr = "Excusez-moi / Pardon", it = "Scusa / Mi scusi",
            ja = "すみません", jaRomaji = "Sumimasen", ko = "실례합니다", koRomaji = "Sillyehamnida",
            tr = "Özür dilerim", ar = "معذرة", arPhonetic = "Ma'dhira", pt = "Com licença / Desculpe",
            hi = "क्षमा करें", kk = "Кешіріңіз", uz = "Kechirasiz", uk = "Вибачте", pl = "Przepraszam",
            category = "Вежливость"
        ),
        LexiconEntry(
            ru = "Простите", en = "Sorry", zh = "对不起", zhPinyin = "Duìbuqǐ",
            es = "Perdón", de = "Es tut mir leid", fr = "Désolé", it = "Scusa",
            ja = "ごめんなさい", jaRomaji = "Gomennasai", ko = "죄송합니다", koRomaji = "Joesonghamnida",
            tr = "Affedersiniz", ar = "آسف", arPhonetic = "Asif", pt = "Desculpe",
            hi = "माफ़ कीजिए", kk = "Ғафу етіңіз", uz = "Uzr", uk = "Пробачте", pl = "Przepraszam",
            category = "Вежливость"
        ),
        LexiconEntry(
            ru = "Да", en = "Yes", zh = "是", zhPinyin = "Shì",
            es = "Sí", de = "Ja", fr = "Oui", it = "Sì",
            ja = "はい", jaRomaji = "Hai", ko = "네", koRomaji = "Ne",
            tr = "Evet", ar = "نعم", arPhonetic = "Na'am", pt = "Sim",
            hi = "हाँ", kk = "Иә", uz = "Ha", uk = "Так", pl = "Tak",
            category = "Базовые"
        ),
        LexiconEntry(
            ru = "Нет", en = "No", zh = "不", zhPinyin = "Bù",
            es = "No", de = "Nein", fr = "Non", it = "No",
            ja = "いいえ", jaRomaji = "Iie", ko = "아니요", koRomaji = "Aniyo",
            tr = "Hayır", ar = "لا", arPhonetic = "La", pt = "Não",
            hi = "नहीं", kk = "Жоқ", uz = "Yo'q", uk = "Ні", pl = "Nie",
            category = "Базовые"
        ),
        LexiconEntry(
            ru = "Хорошо", en = "Good / Okay", zh = "好", zhPinyin = "Hǎo",
            es = "Bien / De acuerdo", de = "Gut / Okay", fr = "Bien / D'accord", it = "Bene / Va bene",
            ja = "いい / オーケー", jaRomaji = "Ii / Ōkē", ko = "좋아요", koRomaji = "Joh-ayo",
            tr = "Tamam / İyi", ar = "حسناً / جيد", arPhonetic = "Hasanan / Jayyid", pt = "Bom / Tudo bem",
            hi = "अच्छा / ठीक है", kk = "Жақсы", uz = "Yaxshi", uk = "Добре", pl = "Dobrze",
            category = "Базовые"
        ),
        LexiconEntry(
            ru = "Как дела?", en = "How are you?", zh = "你好吗？", zhPinyin = "Nǐ hǎo ma?",
            es = "¿Cómo estás?", de = "Wie geht es dir?", fr = "Comment ça va ?", it = "Come stai?",
            ja = "お元気ですか？", jaRomaji = "Ogenki desu ka?", ko = "어떻게 지내세요?", koRomaji = "Eotteohke jinaeseyo?",
            tr = "Nasılsın?", ar = "كيف حالك؟", arPhonetic = "Kayfa haluka?", pt = "Como você está?",
            hi = "आप कैसे हैं?", kk = "Қалыңыз қалай?", uz = "Qalaysiz?", uk = "Як справи?", pl = "Jak się masz?",
            category = "Разговорные"
        ),
        LexiconEntry(
            ru = "Отлично", en = "Great / Excellent", zh = "太好了", zhPinyin = "Tài hǎo le",
            es = "Excelente", de = "Ausgezeichnet", fr = "Excellent", it = "Eccellente",
            ja = "すばらしい", jaRomaji = "Subarashii", ko = "아주 좋아요", koRomaji = "Aju joh-ayo",
            tr = "Harika", ar = "ممتاز", arPhonetic = "Mumtaz", pt = "Excelente",
            hi = "बहुत बढ़िया", kk = "Тамаша", uz = "Ajoyib", uk = "Чудово", pl = "Świetnie",
            category = "Разговорные"
        ),
        LexiconEntry(
            ru = "Как тебя зовут?", en = "What is your name?", zh = "你叫什么名字？", zhPinyin = "Nǐ jiào shénme míngzi?",
            es = "¿Cómo te llamas?", de = "Wie heißt du?", fr = "Comment vous vous appelez ?", it = "Come ti chiami?",
            ja = "お名前は何ですか？", jaRomaji = "Onamae wa nan desu ka?", ko = "이름이 무엇입니까?", koRomaji = "Ireumi mueos-imnikka?",
            tr = "Adın ne?", ar = "ما اسمك؟", arPhonetic = "Ma ismuka?", pt = "Qual é o seu nome?",
            hi = "आपका नाम क्या है?", kk = "Атыңыз кім?", uz = "Ismingiz nima?", uk = "Як тебе звати?", pl = "Jak masz na imię?",
            category = "Разговорные"
        ),
        LexiconEntry(
            ru = "Меня зовут", en = "My name is", zh = "我叫", zhPinyin = "Wǒ jiào",
            es = "Me llamo", de = "Ich heiße", fr = "Je m'appelle", it = "Mi chiamo",
            ja = "私の名前は", jaRomaji = "Watashi no namae wa", ko = "제 이름은", koRomaji = "Je ireum-eun",
            tr = "Benim adım", ar = "اسمي", arPhonetic = "Ismi", pt = "Meu nome é",
            hi = "मेरा नाम है", kk = "Менің атым", uz = "Mening ismim", uk = "Мене звати", pl = "Nazywam się",
            category = "Разговорные"
        ),
        LexiconEntry(
            ru = "Приятно познакомиться", en = "Nice to meet you", zh = "很高兴认识你", zhPinyin = "Hěn gāoxìng rènshi nǐ",
            es = "Mucho gusto", de = "Schön dich kennenzulernen", fr = "Ravi de vous rencontrer", it = "Piacere di conoscerti",
            ja = "はじめまして", jaRomaji = "Hajimemashite", ko = "만나서 반갑습니다", koRomaji = "Mannaseo bangapseumnida",
            tr = "Tanıştığıma memnun oldum", ar = "تشرفت بمعرفتك", arPhonetic = "Tasharraftu bima'rifatik", pt = "Prazer em conhecer",
            hi = "आपसे मिलकर खुशी हुई", kk = "Танысқаныма қуаныштымын", uz = "Tanishganimdan xursandman", uk = "Приємно познайомитися", pl = "Miło cię poznać",
            category = "Разговорные"
        ),
        LexiconEntry(
            ru = "Я не понимаю", en = "I don't understand", zh = "我不明白", zhPinyin = "Wǒ bù míngbai",
            es = "No entiendo", de = "Ich verstehe nicht", fr = "Je ne comprends pas", it = "Non capisco",
            ja = "わかりません", jaRomaji = "Wakarimasen", ko = "이해하지 못했습니다", koRomaji = "Ihae-haji mot-haess-seumnida",
            tr = "Anlamıyorum", ar = "لا أفهم", arPhonetic = "La afham", pt = "Eu não entendo",
            hi = "मुझे समझ नहीं आया", kk = "Мен түсінбеймін", uz = "Men tushunmayapman", uk = "Я не розумію", pl = "Nie rozumiem",
            category = "Разговорные"
        ),
        LexiconEntry(
            ru = "Вы говорите по-английски?", en = "Do you speak English?", zh = "你会说英语吗？", zhPinyin = "Nǐ huì shuō yīngyǔ ma?",
            es = "¿Hablas inglés?", de = "Sprechen Sie Englisch?", fr = "Parlez-vous anglais ?", it = "Parla inglese?",
            ja = "英語を話せますか？", jaRomaji = "Eigo o hanasemasu ka?", ko = "영어 할 수 있나요?", koRomaji = "Yeong-eo hal su issnayo?",
            tr = "İngilizce biliyor musun?", ar = "هل تتحدث الإنجليزية؟", arPhonetic = "Hal tatakallam al-ingliziyya?", pt = "Você fala inglês?",
            hi = "क्या आप अंग्रेज़ी बोलते हैं?", kk = "Ағылшынша сөйлейсіз бе?", uz = "Inglizcha gapirasizmi?", uk = "Ви розмовляєте англійською?", pl = "Czy mówisz po angielsku?",
            category = "Разговорные"
        ),
        LexiconEntry(
            ru = "Помогите", en = "Help", zh = "救命 / 帮助", zhPinyin = "Jiùmìng / Bāngzhù",
            es = "Ayuda", de = "Hilfe", fr = "Aidez-moi", it = "Aiuto",
            ja = "助けて", jaRomaji = "Tasukete", ko = "도와주세요", koRomaji = "Dowajuseyo",
            tr = "İmdat / Yardım", ar = "ساعدني / النجدة", arPhonetic = "Sa'idni / Al-najda", pt = "Socorro / Ajuda",
            hi = "मदद करो", kk = "Көмектесіңіз", uz = "Yordam bering", uk = "Допоможіть", pl = "Pomocy",
            category = "Чрезвычайные"
        ),
        LexiconEntry(
            ru = "Где туалет?", en = "Where is the bathroom?", zh = "洗手间在哪里？", zhPinyin = "Xǐshǒujiān zài nǎlǐ?",
            es = "¿Dónde está el baño?", de = "Wo ist die Toilette?", fr = "Où sont les toilettes ?", it = "Dov'è il bagno?",
            ja = "トイレはどこですか？", jaRomaji = "Toire wa doko desu ka?", ko = "화장실이 어디예요?", koRomaji = "Hwajangsil-i eodiyeyo?",
            tr = "Tuvalet nerede?", ar = "أين الحمام؟", arPhonetic = "Ayna al-hammam?", pt = "Onde fica o banheiro?",
            hi = "शौचालय कहाँ है?", kk = "Дәретхана қайда?", uz = "Hojatxona qayerda?", uk = "Де туалет?", pl = "Gdzie jest toaleta?",
            category = "Путешествия"
        ),
        LexiconEntry(
            ru = "Сколько это стоит?", en = "How much does it cost?", zh = "这个多少钱？", zhPinyin = "Zhège duōshao qián?",
            es = "¿Cuánto cuesta esto?", de = "Wie viel kostet das?", fr = "Combien ça coûte ?", it = "Quanto costa?",
            ja = "これはいくらですか？", jaRomaji = "Kore wa ikura desu ka?", ko = "이것은 얼마예요?", koRomaji = "Igeos-eun eolmayeyo?",
            tr = "Bu ne kadar?", ar = "بكم هذا؟", arPhonetic = "Bikam hadha?", pt = "Quanto custa isto?",
            hi = "यह कितने का है?", kk = "Бұл қанша тұрады?", uz = "Bu qancha turadi?", uk = "Скільки це коштує?", pl = "Ile to kosztuje?",
            category = "Покупки"
        ),
        LexiconEntry(
            ru = "Счет, пожалуйста", en = "The bill, please", zh = "买单，谢谢", zhPinyin = "Mǎidān, xièxie",
            es = "La cuenta, por favor", de = "Die Rechnung, bitte", fr = "L'addition, s'il vous plaît", it = "Il conto, per favore",
            ja = "お会計をお願いします", jaRomaji = "Okaikei o onegaishimasu", ko = "계산서 주세요", koRomaji = "Gyesanseo juseyo",
            tr = "Hesap lütfen", ar = "الحساب لو سمحت", arPhonetic = "Al-hisab law samaht", pt = "A conta, por favor",
            hi = "कृपया बिल दीजिए", kk = "Есепшотты әкеліңізші", uz = "Hisobni bering, iltimos", uk = "Рахунок, будь ласка", pl = "Poproszę rachunek",
            category = "Еда"
        ),
        // Crucial Vocabulary Words
        LexiconEntry(
            ru = "Вода", en = "Water", zh = "水", zhPinyin = "Shuǐ",
            es = "Agua", de = "Wasser", fr = "Eau", it = "Acqua",
            ja = "水", jaRomaji = "Mizu", ko = "물", koRomaji = "Mul",
            tr = "Su", ar = "ماء", arPhonetic = "Ma'", pt = "Água",
            hi = "पानी", kk = "Су", uz = "Suv", uk = "Вода", pl = "Woda",
            category = "Еда"
        ),
        LexiconEntry(
            ru = "Еда", en = "Food", zh = "食物", zhPinyin = "Shíwù",
            es = "Comida", de = "Essen", fr = "Nourriture", it = "Cibo",
            ja = "食べ物", jaRomaji = "Tabemono", ko = "음식", koRomaji = "Eumsik",
            tr = "Yemek", ar = "طعام", arPhonetic = "Ta'am", pt = "Comida",
            hi = "खाना", kk = "Тамақ", uz = "Ovqat", uk = "Їжа", pl = "Jedzenie",
            category = "Еда"
        ),
        LexiconEntry(
            ru = "Чай", en = "Tea", zh = "茶", zhPinyin = "Chá",
            es = "Té", de = "Tee", fr = "Thé", it = "Tè",
            ja = "お茶", jaRomaji = "Ocha", ko = "차", koRomaji = "Cha",
            tr = "Çay", ar = "شاي", arPhonetic = "Shay", pt = "Chá",
            hi = "चाय", kk = "Шай", uz = "Choy", uk = "Чай", pl = "Herbata",
            category = "Еда"
        ),
        LexiconEntry(
            ru = "Кофе", en = "Coffee", zh = "咖啡", zhPinyin = "Kāfēi",
            es = "Café", de = "Kaffee", fr = "Café", it = "Caffè",
            ja = "コーヒー", jaRomaji = "Kōhī", ko = "커피", koRomaji = "Keopi",
            tr = "Kahve", ar = "قهوة", arPhonetic = "Qahwa", pt = "Café",
            hi = "कॉफ़ी", kk = "Кофе", uz = "Kofe", uk = "Кава", pl = "Kawa",
            category = "Еда"
        ),
        LexiconEntry(
            ru = "Хлеб", en = "Bread", zh = "面包", zhPinyin = "Miànbāo",
            es = "Pan", de = "Brot", fr = "Pain", it = "Pane",
            ja = "パン", jaRomaji = "Pan", ko = "빵", koRomaji = "Ppang",
            tr = "Ekmek", ar = "خبز", arPhonetic = "Khubz", pt = "Pão",
            hi = "रोटी", kk = "Нан", uz = "Non", uk = "Хліб", pl = "Chleb",
            category = "Еда"
        ),
        LexiconEntry(
            ru = "Друг", en = "Friend", zh = "朋友", zhPinyin = "Péngyou",
            es = "Amigo", de = "Freund", fr = "Ami", it = "Amico",
            ja = "友達", jaRomaji = "Tomodachi", ko = "친구", koRomaji = "Chingu",
            tr = "Arkadaş", ar = "صديق", arPhonetic = "Sadiq", pt = "Amigo",
            hi = "दोस्त", kk = "Дос", uz = "Do'st", uk = "Друг", pl = "Przyjaciel",
            category = "Семья"
        ),
        LexiconEntry(
            ru = "Семья", en = "Family", zh = "家庭", zhPinyin = "Jiātíng",
            es = "Familia", de = "Familie", fr = "Famille", it = "Famiglia",
            ja = "家族", jaRomaji = "Kazoku", ko = "가족", koRomaji = "Gajok",
            tr = "Aile", ar = "عائلة", arPhonetic = "A'ila", pt = "Família",
            hi = "परिवार", kk = "Отбасы", uz = "Oila", uk = "Сім'я", pl = "Rodzina",
            category = "Семья"
        ),
        LexiconEntry(
            ru = "Любовь", en = "Love", zh = "爱", zhPinyin = "Ài",
            es = "Amor", de = "Liebe", fr = "Amour", it = "Amore",
            ja = "愛", jaRomaji = "Ai", ko = "사랑", koRomaji = "Sarang",
            tr = "Aşk", ar = "حب", arPhonetic = "Hubb", pt = "Amor",
            hi = "प्यार", kk = "Махаббат", uz = "Sevgi", uk = "Кохання", pl = "Miłość",
            category = "Чувства"
        ),
        LexiconEntry(
            ru = "Дом", en = "Home / House", zh = "家", zhPinyin = "Jiā",
            es = "Casa / Hogar", de = "Haus / Heim", fr = "Maison", it = "Casa",
            ja = "家", jaRomaji = "Ie", ko = "집", koRomaji = "Jip",
            tr = "Ev", ar = "بيت / منزل", arPhonetic = "Bayt / Manzil", pt = "Casa",
            hi = "घर", kk = "Үй", uz = "Uy", uk = "Дім", pl = "Dom",
            category = "Места"
        ),
        LexiconEntry(
            ru = "Книга", en = "Book", zh = "书", zhPinyin = "Shū",
            es = "Libro", de = "Buch", fr = "Livre", it = "Libro",
            ja = "本", jaRomaji = "Hon", ko = "책", koRomaji = "Chaek",
            tr = "Kitap", ar = "كتاب", arPhonetic = "Kitab", pt = "Livro",
            hi = "किताब", kk = "Кітап", uz = "Kitob", uk = "Книга", pl = "Książka",
            category = "Обучение"
        ),
        LexiconEntry(
            ru = "Учить", en = "Learn / Study", zh = "学", zhPinyin = "Xué",
            es = "Aprender", de = "Lernen", fr = "Apprendre", it = "Imparare",
            ja = "学ぶ", jaRomaji = "Manabu", ko = "배우다", koRomaji = "Baeuda",
            tr = "Öğrenmek", ar = "تعلم", arPhonetic = "Ta'allam", pt = "Aprender",
            hi = "सीखना", kk = "Үйрену", uz = "O'rganmoq", uk = "Вчити", pl = "Uczyć się",
            category = "Обучение"
        ),
        LexiconEntry(
            ru = "Язык", en = "Language", zh = "语言", zhPinyin = "Yǔyán",
            es = "Idioma", de = "Sprache", fr = "Langue", it = "Lingua",
            ja = "言語", jaRomaji = "Gengo", ko = "언어", koRomaji = "Eoneo",
            tr = "Dil", ar = "لغة", arPhonetic = "Lugha", pt = "Idioma / Língua",
            hi = "भाषा", kk = "Тіл", uz = "Til", uk = "Мова", pl = "Język",
            category = "Обучение"
        ),
        LexiconEntry(
            ru = "Слово", en = "Word", zh = "词 / 字", zhPinyin = "Cí",
            es = "Palabra", de = "Wort", fr = "Mot", it = "Parola",
            ja = "言葉", jaRomaji = "Kotoba", ko = "단어", koRomaji = "Daneo",
            tr = "Kelime", ar = "كلمة", arPhonetic = "Kalima", pt = "Palavra",
            hi = "शब्द", kk = "Сөз", uz = "So'z", uk = "Слово", pl = "Słowo",
            category = "Обучение"
        ),
        LexiconEntry(
            ru = "Деньги", en = "Money", zh = "钱", zhPinyin = "Qián",
            es = "Dinero", de = "Geld", fr = "Argent", it = "Denaro",
            ja = "お金", jaRomaji = "Okane", ko = "돈", koRomaji = "Don",
            tr = "Para", ar = "مال / نقود", arPhonetic = "Mal / Nuqud", pt = "Dinheiro",
            hi = "पैसा", kk = "Ақша", uz = "Pul", uk = "Гроші", pl = "Pieniądze",
            category = "Покупки"
        ),
        LexiconEntry(
            ru = "Время", en = "Time", zh = "时间", zhPinyin = "Shíjiān",
            es = "Tiempo / Hora", de = "Zeit", fr = "Temps / Heure", it = "Tempo / Ora",
            ja = "時間", jaRomaji = "Jikan", ko = "시간", koRomaji = "Sigan",
            tr = "Zaman / Vakit", ar = "وقت / زمن", arPhonetic = "Waqt", pt = "Tempo / Hora",
            hi = "समय", kk = "Уақыт", uz = "Vaqt", uk = "Час", pl = "Czas",
            category = "Базовые"
        ),
        LexiconEntry(
            ru = "Отель", en = "Hotel", zh = "酒店", zhPinyin = "Jiǔdiàn",
            es = "Hotel", de = "Hotel", fr = "Hôtel", it = "Hotel",
            ja = "ホテル", jaRomaji = "Hoteru", ko = "호텔", koRomaji = "Hotel",
            tr = "Otel", ar = "فندق", arPhonetic = "Funduq", pt = "Hotel",
            hi = "होटल", kk = "Қонақ үй", uz = "Mehmonxona", uk = "Готель", pl = "Hotel",
            category = "Путешествия"
        ),
        LexiconEntry(
            ru = "Аэропорт", en = "Airport", zh = "机场", zhPinyin = "Jīchǎng",
            es = "Aeropuerto", de = "Flughafen", fr = "Aéroport", it = "Aeroporto",
            ja = "空港", jaRomaji = "Kūkō", ko = "공항", koRomaji = "Gonghang",
            tr = "Havalimanı", ar = "مطار", arPhonetic = "Matar", pt = "Aeroporto",
            hi = "हवाई अड्डा", kk = "Әуежай", uz = "Aeroport", uk = "Аеропорт", pl = "Lotnisko",
            category = "Путешествия"
        ),
        LexiconEntry(
            ru = "Поезд", en = "Train", zh = "火车", zhPinyin = "Huǒchē",
            es = "Tren", de = "Zug", fr = "Train", it = "Treno",
            ja = "電車", jaRomaji = "Densha", ko = "기차", koRomaji = "Gicha",
            tr = "Tren", ar = "قطار", arPhonetic = "Qitar", pt = "Trem",
            hi = "रेलगाड़ी", kk = "Пойыз", uz = "Poyezd", uk = "Поїзд", pl = "Pociąg",
            category = "Путешествия"
        ),
        LexiconEntry(
            ru = "Автобус", en = "Bus", zh = "公交车", zhPinyin = "Gōngjiāochē",
            es = "Autobús", de = "Bus", fr = "Bus", it = "Autobus",
            ja = "バス", jaRomaji = "Basu", ko = "버스", koRomaji = "Beoseu",
            tr = "Otobüs", ar = "حافلة", arPhonetic = "Hafila", pt = "Ônibus",
            hi = "बस", kk = "Автобус", uz = "Avtobus", uk = "Автобус", pl = "Autobus",
            category = "Путешествия"
        ),
        LexiconEntry(
            ru = "Улица", en = "Street", zh = "街道", zhPinyin = "Jiēdào",
            es = "Calle", de = "Straße", fr = "Rue", it = "Strada",
            ja = "通り", jaRomaji = "Tōri", ko = "거리", koRomaji = "Geori",
            tr = "Sokak / Cadde", ar = "شارع", arPhonetic = "Shari'", pt = "Rua",
            hi = "सड़क", kk = "Көше", uz = "Ko'cha", uk = "Вулиця", pl = "Ulica",
            category = "Путешествия"
        ),
        LexiconEntry(
            ru = "Город", en = "City", zh = "城市", zhPinyin = "Chéngshì",
            es = "Ciudad", de = "Stadt", fr = "Ville", it = "Città",
            ja = "街 / 都市", jaRomaji = "Machi / Toshi", ko = "도시", koRomaji = "Dosi",
            tr = "Şehir", ar = "مدينة", arPhonetic = "Madina", pt = "Cidade",
            hi = "शहर", kk = "Қала", uz = "Shahar", uk = "Місто", pl = "Miasto",
            category = "Путешествия"
        ),
        LexiconEntry(
            ru = "Страна", en = "Country", zh = "国家", zhPinyin = "Guójiā",
            es = "País", de = "Land", fr = "Pays", it = "Paese",
            ja = "国", jaRomaji = "Kuni", ko = "나라", koRomaji = "Nara",
            tr = "Ülke", ar = "بلد / دولة", arPhonetic = "Balad", pt = "País",
            hi = "देश", kk = "Ел", uz = "Mamlakat", uk = "Країна", pl = "Kraj",
            category = "Путешествия"
        ),
        LexiconEntry(
            ru = "Мир", en = "World / Peace", zh = "世界", zhPinyin = "Shìjiè",
            es = "Mundo / Paz", de = "Welt / Frieden", fr = "Monde / Paix", it = "Mondo / Pace",
            ja = "世界 / 平和", jaRomaji = "Sekai", ko = "세계 / 평화", koRomaji = "Segye",
            tr = "Dünya / Barış", ar = "العالم / سلام", arPhonetic = "Al-'alam", pt = "Mundo / Paz",
            hi = "दुनिया / शांति", kk = "Әлем", uz = "Dunyo", uk = "Світ", pl = "Świat",
            category = "Базовые"
        ),
        LexiconEntry(
            ru = "Солнце", en = "Sun", zh = "太阳", zhPinyin = "Tàiyáng",
            es = "Sol", de = "Sonne", fr = "Soleil", it = "Sole",
            ja = "太陽", jaRomaji = "Taiyō", ko = "태양", koRomaji = "Taeyang",
            tr = "Güneş", ar = "شمس", arPhonetic = "Shams", pt = "Sol",
            hi = "सूर्य", kk = "Күн", uz = "Quyosh", uk = "Сонце", pl = "Słońce",
            category = "Природа"
        ),
        LexiconEntry(
            ru = "Луна", en = "Moon", zh = "月亮", zhPinyin = "Yuèliang",
            es = "Luna", de = "Mond", fr = "Lune", it = "Luna",
            ja = "月", jaRomaji = "Tsuki", ko = "달", koRomaji = "Dal",
            tr = "Ay", ar = "قمر", arPhonetic = "Qamar", pt = "Lua",
            hi = "चाँद", kk = "Ай", uz = "Oy", uk = "Місяць", pl = "Księżyc",
            category = "Природа"
        ),
        LexiconEntry(
            ru = "Кошка", en = "Cat", zh = "猫", zhPinyin = "Māo",
            es = "Gato", de = "Katze", fr = "Chat", it = "Gatto",
            ja = "猫", jaRomaji = "Neko", ko = "고양이", koRomaji = "Goyangi",
            tr = "Kedi", ar = "قطة", arPhonetic = "Qitta", pt = "Gato",
            hi = "बिल्ली", kk = "Мысық", uz = "Mushuk", uk = "Кішка", pl = "Kot",
            category = "Животные"
        ),
        LexiconEntry(
            ru = "Собака", en = "Dog", zh = "狗", zhPinyin = "Gǒu",
            es = "Perro", de = "Hund", fr = "Chien", it = "Cane",
            ja = "犬", jaRomaji = "Inu", ko = "개", koRomaji = "Gae",
            tr = "Köpek", ar = "كلب", arPhonetic = "Kalb", pt = "Cachorro",
            hi = "कुत्ता", kk = "Ит", uz = "It", uk = "Собака", pl = "Pies",
            category = "Животные"
        ),
        LexiconEntry(
            ru = "Один", en = "One", zh = "一", zhPinyin = "Yī",
            es = "Uno", de = "Eins", fr = "Un", it = "Uno",
            ja = "一 (いち)", jaRomaji = "Ichi", ko = "일 (하나)", koRomaji = "Il (Hana)",
            tr = "Bir", ar = "واحد", arPhonetic = "Wahid", pt = "Um",
            hi = "एक", kk = "Бір", uz = "Bir", uk = "Один", pl = "Jeden",
            category = "Числа"
        ),
        LexiconEntry(
            ru = "Два", en = "Two", zh = "二", zhPinyin = "Èr",
            es = "Dos", de = "Zwei", fr = "Deux", it = "Due",
            ja = "二 (に)", jaRomaji = "Ni", ko = "이 (둘)", koRomaji = "I (Dul)",
            tr = "İki", ar = "اثنان", arPhonetic = "Ithnan", pt = "Dois",
            hi = "दो", kk = "Екі", uz = "Ikki", uk = "Два", pl = "Dwa",
            category = "Числа"
        ),
        LexiconEntry(
            ru = "Три", en = "Three", zh = "三", zhPinyin = "Sān",
            es = "Tres", de = "Drei", fr = "Trois", it = "Tre",
            ja = "三 (さん)", jaRomaji = "San", ko = "삼 (셋)", koRomaji = "Sam (Set)",
            tr = "Üç", ar = "ثلاثة", arPhonetic = "Thalatha", pt = "Três",
            hi = "तीन", kk = "Үш", uz = "Uch", uk = "Три", pl = "Trzy",
            category = "Числа"
        ),
        LexiconEntry(
            ru = "Красивый", en = "Beautiful", zh = "美丽 / 漂亮", zhPinyin = "Měilì / Piàoliang",
            es = "Hermoso", de = "Schön", fr = "Beau / Belle", it = "Bello",
            ja = "美しい / きれい", jaRomaji = "Utsukushii / Kirei", ko = "아름다운", koRomaji = "Areumdaun",
            tr = "Güzel", ar = "جميل", arPhonetic = "Jamil", pt = "Bonito",
            hi = "सुंदर", kk = "Әдемі", uz = "Chiroyli", uk = "Красивий", pl = "Piękny",
            category = "Описания"
        ),
        LexiconEntry(
            ru = "Большой", en = "Big / Large", zh = "大", zhPinyin = "Dà",
            es = "Grande", de = "Groß", fr = "Grand", it = "Grande",
            ja = "大きい", jaRomaji = "Ōkii", ko = "큰", koRomaji = "Keun",
            tr = "Büyük", ar = "كبير", arPhonetic = "Kabir", pt = "Grande",
            hi = "बड़ा", kk = "Үлкен", uz = "Katta", uk = "Великий", pl = "Duży",
            category = "Описания"
        ),
        LexiconEntry(
            ru = "Маленький", en = "Small / Little", zh = "小", zhPinyin = "Xiǎo",
            es = "Pequeño", de = "Klein", fr = "Petit", it = "Piccolo",
            ja = "小さい", jaRomaji = "Chiisai", ko = "작은", koRomaji = "Jageun",
            tr = "Küçük", ar = "صغير", arPhonetic = "Saghir", pt = "Pequeno",
            hi = "छोटा", kk = "Кішкентай", uz = "Kichik", uk = "Маленький", pl = "Mały",
            category = "Описания"
        ),
        LexiconEntry(
            ru = "Я хочу", en = "I want", zh = "我想要", zhPinyin = "Wǒ xiǎng yào",
            es = "Quiero", de = "Ich möchte", fr = "Je veux", it = "Voglio",
            ja = "欲しい", jaRomaji = "Hoshii", ko = "원해요", koRomaji = "Wonhaeyo",
            tr = "İstiyorum", ar = "أريد", arPhonetic = "Urid", pt = "Eu quero",
            hi = "मुझे चाहिए", kk = "Мен қалаймын", uz = "Men xohlayman", uk = "Я хочу", pl = "Chcę",
            category = "Разговорные"
        ),
        LexiconEntry(
            ru = "Я люблю тебя", en = "I love you", zh = "我爱你", zhPinyin = "Wǒ ài nǐ",
            es = "Te quiero / Te amo", de = "Ich liebe dich", fr = "Je t'aime", it = "Ti amo",
            ja = "愛してる", jaRomaji = "Aishiteru", ko = "사랑해요", koRomaji = "Saranghaeyo",
            tr = "Seni seviyorum", ar = "أحبك", arPhonetic = "Uhibbuka", pt = "Eu te amo",
            hi = "मैं तुमसे प्यार करता हूँ", kk = "Мен сені сүйемін", uz = "Men seni sevaman", uk = "Я тебе кохаю", pl = "Kocham cię",
            category = "Чувства"
        ),
        LexiconEntry(
            ru = "Вкусный", en = "Delicious", zh = "好吃", zhPinyin = "Hǎochī",
            es = "Delicioso", de = "Lecker", fr = "Délicieux", it = "Delizioso",
            ja = "おいしい", jaRomaji = "Oishii", ko = "맛있는", koRomaji = "Mas-issneun",
            tr = "Lezzetli", ar = "لذيذ", arPhonetic = "Ladhidh", pt = "Gostoso",
            hi = "स्वादिष्ट", kk = "Дәмді", uz = "Mazali", uk = "Смачний", pl = "Pyszny",
            category = "Еда"
        ),
        LexiconEntry(
            ru = "Где я?", en = "Where am I?", zh = "我在哪里？", zhPinyin = "Wǒ zài nǎlǐ?",
            es = "¿Dónde estoy?", de = "Wo bin ich?", fr = "Où suis-je ?", it = "Dove sono?",
            ja = "私はどこにいますか？", jaRomaji = "Watashi wa doko ni imasu ka?", ko = "내가 어디에 있나요?", koRomaji = "Naega eodie issnayo?",
            tr = "Neredeyim?", ar = "أين أنا؟", arPhonetic = "Ayna ana?", pt = "Onde estou?",
            hi = "मैं कहाँ हूँ?", kk = "Мен қайдамын?", uz = "Men qayerdaman?", uk = "Де я?", pl = "Gdzie jestem?",
            category = "Путешествия"
        ),
        LexiconEntry(
            ru = "Прямо", en = "Straight ahead", zh = "直走", zhPinyin = "Zhí zǒu",
            es = "Todo recto", de = "Geradeaus", fr = "Tout droit", it = "Dritto",
            ja = "まっすぐ", jaRomaji = "Massugu", ko = "똑바로", koRomaji = "Ttokbaro",
            tr = "Düz", ar = "إلى الأمام مباشرة", arPhonetic = "Ila al-amam", pt = "Em frente",
            hi = "सीधे", kk = "Тіке", uz = "To'g'riga", uk = "Прямо", pl = "Prosto",
            category = "Навигация"
        ),
        LexiconEntry(
            ru = "Налево", en = "To the left", zh = "向左", zhPinyin = "Xiàng zuǒ",
            es = "A la izquierda", de = "Nach links", fr = "À gauche", it = "A sinistra",
            ja = "左へ", jaRomaji = "Hidari e", ko = "왼쪽으로", koRomaji = "Oenjjog-euro",
            tr = "Sola", ar = "إلى اليسار", arPhonetic = "Ila al-yasar", pt = "Para a esquerda",
            hi = "बाईं ओर", kk = "Солға", uz = "Chapga", uk = "Ліворуч", pl = "W lewo",
            category = "Навигация"
        ),
        LexiconEntry(
            ru = "Направо", en = "To the right", zh = "向右", zhPinyin = "Xiàng yòu",
            es = "A la derecha", de = "Nach rechts", fr = "À droite", it = "A destra",
            ja = "右へ", jaRomaji = "Migi e", ko = "오른쪽으로", koRomaji = "Oreunjjog-euro",
            tr = "Sağa", ar = "إلى اليمين", arPhonetic = "Ila al-yamin", pt = "Para a direita",
            hi = "दाईं ओर", kk = "Оңға", uz = "O'ngga", uk = "Праворуч", pl = "W prawo",
            category = "Навигация"
        )
    )

    fun translate(
        input: String,
        sourceLang: String,
        targetLang: String
    ): TranslationResult {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) {
            return TranslationResult("", "", trimmed, sourceLang, targetLang)
        }

        // If source and target are the same language
        if (sourceLang.equals(targetLang, ignoreCase = true)) {
            val phonetic = getPhonetic(trimmed, targetLang)
            return TranslationResult(trimmed, phonetic, trimmed, sourceLang, targetLang)
        }

        // 1. Direct whole-string match against the lexicon
        val directMatch = findExactMatch(trimmed, sourceLang, targetLang)
        if (directMatch != null) {
            return directMatch
        }

        // 2. Case-insensitive whole-string match
        val caseMatch = findCaseInsensitiveMatch(trimmed, sourceLang, targetLang)
        if (caseMatch != null) {
            return caseMatch
        }

        // 3. Multi-word sentence / tokenized translation
        val tokens = trimmed.split(Regex("(?<=\\s)|(?=\\s)|(?<=[.,!?;:\"()])|(?=[.,!?;:\"()])"))
        val translatedTokens = StringBuilder()
        var hasAnyWordTranslated = false

        for (token in tokens) {
            val cleanToken = token.trim()
            if (cleanToken.isEmpty() || cleanToken.matches(Regex("[.,!?;:\"()\\s]+"))) {
                translatedTokens.append(token)
                continue
            }

            val tokenMatch = findCaseInsensitiveMatch(cleanToken, sourceLang, targetLang)
            if (tokenMatch != null) {
                // preserve uppercase if original was capitalized
                val word = if (cleanToken.firstOrNull()?.isUpperCase() == true) {
                    tokenMatch.translatedText.replaceFirstChar { it.uppercase() }
                } else {
                    tokenMatch.translatedText
                }
                translatedTokens.append(word)
                hasAnyWordTranslated = true
            } else {
                translatedTokens.append(token)
            }
        }

        val resultText = if (hasAnyWordTranslated) {
            translatedTokens.toString()
        } else {
            // Intelligent transliteration or language script mapping
            transliterateOrAdapt(trimmed, sourceLang, targetLang)
        }

        val phonetic = getPhonetic(resultText, targetLang)
        return TranslationResult(
            translatedText = resultText,
            phonetic = phonetic,
            originalText = trimmed,
            sourceLangCode = sourceLang,
            targetLangCode = targetLang,
            isExactMatch = hasAnyWordTranslated,
            notes = if (!hasAnyWordTranslated) "Оффлайн режим (транслитерация / базовый перевод)" else ""
        )
    }

    private fun findExactMatch(text: String, srcLang: String, trgLang: String): TranslationResult? {
        val entry = VOCABULARY.find { getLangValue(it, srcLang) == text } ?: return null
        val trgValue = getLangValue(entry, trgLang)
        if (trgValue.isEmpty()) return null
        val phonetic = getLangPhonetic(entry, trgLang)
        return TranslationResult(
            translatedText = trgValue,
            phonetic = phonetic,
            originalText = text,
            sourceLangCode = srcLang,
            targetLangCode = trgLang,
            isExactMatch = true,
            notes = entry.category
        )
    }

    private fun findCaseInsensitiveMatch(text: String, srcLang: String, trgLang: String): TranslationResult? {
        val cleanText = text.trim().lowercase().removeSuffix("?").removeSuffix("!").removeSuffix(".")
        val entry = VOCABULARY.find {
            val valInSrc = getLangValue(it, srcLang).trim().lowercase().removeSuffix("?").removeSuffix("!").removeSuffix(".")
            valInSrc == cleanText || valInSrc.split(" / ").any { p -> p.trim().lowercase() == cleanText }
        } ?: return null

        val trgValue = getLangValue(entry, trgLang)
        if (trgValue.isEmpty()) return null
        val phonetic = getLangPhonetic(entry, trgLang)
        return TranslationResult(
            translatedText = trgValue,
            phonetic = phonetic,
            originalText = text,
            sourceLangCode = srcLang,
            targetLangCode = trgLang,
            isExactMatch = true,
            notes = entry.category
        )
    }

    private fun getLangValue(entry: LexiconEntry, langCode: String): String {
        return when (langCode.lowercase().take(2)) {
            "ru" -> entry.ru
            "en" -> entry.en
            "zh" -> entry.zh
            "es" -> entry.es
            "de" -> entry.de
            "fr" -> entry.fr
            "it" -> entry.it
            "ja" -> entry.ja
            "ko" -> entry.ko
            "tr" -> entry.tr
            "ar" -> entry.ar
            "pt" -> entry.pt
            "hi" -> entry.hi
            "kk" -> entry.kk.ifEmpty { entry.ru }
            "uz" -> entry.uz.ifEmpty { entry.tr }
            "uk" -> entry.uk.ifEmpty { entry.ru }
            "pl" -> entry.pl.ifEmpty { entry.en }
            else -> entry.en
        }
    }

    private fun getLangPhonetic(entry: LexiconEntry, langCode: String): String {
        return when (langCode.lowercase().take(2)) {
            "zh" -> entry.zhPinyin
            "ja" -> entry.jaRomaji
            "ko" -> entry.koRomaji
            "ar" -> entry.arPhonetic
            else -> ""
        }
    }

    // Phonetic generator for scripts without native Latin alphabet (Pinyin, Romaji, Romanization)
    fun getPhonetic(text: String, langCode: String): String {
        return when (langCode.lowercase().take(2)) {
            "zh" -> toPinyin(text)
            "ru" -> cyrillicToLatin(text)
            "uk", "kk", "be", "bg", "sr", "mk" -> cyrillicToLatin(text)
            "ja" -> if (text.any { it in '\u3040'..'\u30ff' || it in '\u4e00'..'\u9fa5' }) "Romaji: " + cyrillicToLatin(text) else ""
            "ar" -> "Фонетика: " + cyrillicToLatin(text)
            else -> ""
        }
    }

    // Generates Pinyin for common Chinese characters & words
    fun toPinyin(text: String): String {
        val pinyinMap = mapOf(
            "你" to "nǐ", "好" to "hǎo", "您" to "nín", "吗" to "ma",
            "谢" to "xiè", "不" to "bù", "客" to "kè", "气" to "qi",
            "对" to "duì", "起" to "qǐ", "再" to "zài", "见" to "jiàn",
            "早" to "zǎo", "上" to "shang", "晚" to "wǎn", "安" to "ān",
            "水" to "shuǐ", "茶" to "chá", "咖" to "kā", "啡" to "fēi",
            "吃" to "chī", "饭" to "fàn", "买" to "mǎi", "单" to "dān",
            "钱" to "qián", "多" to "duō", "少" to "shao", "在" to "zài",
            "哪" to "nǎ", "里" to "lǐ", "是" to "shì", "我" to "wǒ",
            "叫" to "jiào", "什" to "shén", "么" to "me", "名" to "míng",
            "字" to "zi", "大" to "dà", "小" to "xiǎo", "爱" to "ài",
            "朋" to "péng", "友" to "you", "家" to "jiā", "学" to "xué",
            "书" to "shū", "猫" to "māo", "狗" to "gǒu", "救" to "jiù",
            "命" to "mìng", "帮" to "bāng", "助" to "zhù", "火" to "huǒ",
            "车" to "chē", "机" to "jī", "场" to "chǎng", "店" to "diàn",
            "酒" to "jiǔ", "城" to "chéng", "市" to "shì", "国" to "guó"
        )
        val sb = StringBuilder()
        var hasPinyin = false
        for (ch in text) {
            val py = pinyinMap[ch.toString()]
            if (py != null) {
                if (sb.isNotEmpty()) sb.append(" ")
                sb.append(py)
                hasPinyin = true
            } else {
                sb.append(ch)
            }
        }
        return if (hasPinyin) sb.toString() else ""
    }

    fun cyrillicToLatin(text: String): String {
        val map = mapOf(
            'а' to "a", 'б' to "b", 'в' to "v", 'г' to "g", 'д' to "d",
            'е' to "e", 'ё' to "yo", 'ж' to "zh", 'з' to "z", 'и' to "i",
            'й' to "y", 'к' to "k", 'л' to "l", 'м' to "m", 'н' to "n",
            'о' to "o", 'п' to "p", 'р' to "r", 'с' to "s", 'т' to "t",
            'у' to "u", 'ф' to "f", 'х' to "kh", 'ц' to "ts", 'ч' to "ch",
            'ш' to "sh", 'щ' to "shch", 'ъ' to "", 'ы' to "y", 'ь' to "",
            'э' to "e", 'ю' to "yu", 'я' to "ya"
        )
        val sb = StringBuilder()
        for (c in text) {
            val lower = c.lowercaseChar()
            val repl = map[lower]
            if (repl != null) {
                sb.append(if (c.isUpperCase()) repl.replaceFirstChar { it.uppercase() } else repl)
            } else {
                sb.append(c)
            }
        }
        return sb.toString()
    }

    private fun transliterateOrAdapt(text: String, srcLang: String, trgLang: String): String {
        // If target uses Latin and source is Cyrillic
        if ((srcLang == "ru" || srcLang == "uk" || srcLang == "kk") && trgLang !in listOf("ru", "uk", "kk", "bg", "sr", "mk", "be")) {
            return cyrillicToLatin(text)
        }
        return text
    }
}
