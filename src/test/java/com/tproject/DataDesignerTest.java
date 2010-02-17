package com.tproject;

import org.apache.wicket.util.tester.WicketTester;

import junit.framework.TestCase;
/**
 * 
 * @author Oh
 *
 */
public class DataDesignerTest extends TestCase{
	private WicketTester tester;
	private DataDesigner data;
	/**
	 * 初期化
	 */
	
	@Override
	public void setUp() {
		tester = new WicketTester(new WicketApplication());
		data = new  DataDesigner();
	}
	/**
	 * アルファベットの大文字をランダムに生成
	 */
	public void testUppercase(){
		String str = "[A-Z]";
		String upper = data.Uppercase();
	
		assertTrue(upper.matches(str));
	}
	/**
	 * アルファベットの小文字をランダムに生成
	 */
	public void testLowercase(){
		String str ="[a-z]";
		String lower = data.Lowercase();
		
		assertTrue(lower.matches(str));
	}
	/**
	 * 数字のランダム生成
	 */
	public void testFigure(){
		String str = "[0-9]";
		String figure = String.valueOf(data.Figure());
		
		assertTrue(figure.matches(str));
	}
	/**
	 * 
	 */
	public void testUpperlower(){
		String str ="[A-Za-z]";
		String lower = data.Upperlower();
		
		assertTrue(lower.matches(str));
	}
	public void testUpperfigure(){
		String str ="[A-Z0-9]";
		String lower = data.Upperfigure();
		
		assertTrue(lower.matches(str));
	}
	public void testLowerfigure(){
		String str ="[a-z0-9]";
		String lower = data.Lowerfigure();
		
		assertTrue(lower.matches(str));
	}
	public void testAllcase(){
		String str ="[A-Za-z0-9]";
		String lower = data.Allcase();
		
		assertTrue(lower.matches(str));
	}
	public void testAddress(){
		String str = "(北海道|青森県|岩手県|宮城県|秋田県|山形県|福島県|茨城県|栃木県|群馬県|埼玉県|千葉県|東京都|神奈川県|新潟県|富山県|石川県|福井県|山梨県|長野県|岐阜県|静岡県|愛知県|三重県|滋賀県|京都府|大阪府|兵庫県|奈良県|和歌山県|鳥取県|島根県|岡山県|広島県|山口県|徳島県|香川県|愛媛県|高知県|福岡県|佐賀県|長崎県|熊本県|大分県|宮崎県|鹿児島県|沖縄県)";
		String address = data.Address();
		
		assertTrue(address.matches(str));
	}
	public void testName(){
		String str1 = "(饗庭|青木|青山|赤木|赤坂|明石|赤松|秋山|明智|浅野|朝日|蘆田|麻生|足利|阿多|安室|荒川|有田|有馬|愛子|飯尾|飯島|飯田|飯沼|生田|井口|池田|石川|石垣|一色|石塔|石野|石橋|石谷|泉|泉出|磯野|板倉|伊那|井上|猪子|今川|入野屋|岩淵|岩間|岩松|植田|上地|植村|上野|上野山|上原|魚往|宇野|浦野|江川|江見|及川|大池|大木|大河内|大島|大崎|太田|大竹|大窪|大寺|大野|大森|大山|岡本|奥|奥田|奥平|小河|小国|小椋|尾崎|小鹿|小田|落合|小原|小俣|恩田|各務|蔭山|加古|柏原|春日部|片切|金井|金森|可児|蟹沢|釜内|神出|上条|上山|川島|河辺|神吉|蒲原|岸本|木田|喜連川|衣笠|吉良|櫛田|櫛橋|久保|黒川|小泉|上月|小島|小寺|小林|榊原|真田|佐用|沢井|沢田|志岐|重田|七条|品川|斯波|渋川|島田|下山|清水|志和|神野|新免|須賀|菅井|菅名|菅沼|杉田|諏訪部|瀬尾|関口|瀬名|高島|高楡|鷹巣|瀧沢|田島|多治見|高井|高木|高田|孝橋|高屋|高山|竹田|竹中|多田|楯岡|田中|谷地|田部井|垂木|知久|津川|辻|土屋|土山|堤|豊島(手島)|手塚|寺井|天童|東|遠山|戸賀崎|戸坂|戸崎|土岐|得平|富塚|外山|豊田|長岡|中川|長沢|中島|中野|永野|中村|中山|名倉|夏目|成瀬|新野（入野）|仁木|西|西尾|二本松|丹羽|能勢|野中|萩原|畠山|蜂須賀|蜂屋|羽仁|花房|馬場|原|林田|葉山|東根|彦坂|土方|肥田|平井|平岡|平田|平野|広岡|広沢|広瀬|福島|福田|福知|福原|船木|不破|別所|穂積|細川|堀|堀越|本郷|前川|間島|松岡|松倉|松本|三木|三刀屋|水上|水田|水野|向井|村井|村田|村山|持永|桃井|森|安井|矢田|柳|矢吹|山県|山田|山野辺|吉野|依田|和賀|若林|脇田)";
		String str2 = "(大翔|樋口|悠斗|陽向|翔太|悠|颯太|悠太|翔|蓮|駿|陸|悠真|瑛太|大輝|陽斗|響|大樹|大和|陽|陸斗|一真|優|雄大|龍之介|翔大|海斗|輝|優斗|悠希|翼|琉生|樹|奏太|大雅|拓海|優翔|陽翔|健太|蒼空|太陽|大悟|琉斗|琥太郎|一輝|健斗|大地|隼|歩夢|優太|悠樹|悠翔|陽太|和真|翔真|一颯|啓太|結|航輝|蒼|大晴|大智|大輔|拓真|隼|優希|優|遥斗|涼太|怜央|颯|颯|永遠|瑛斗|空|圭|健心|虎太郎|鼓太郎|康太|航|春希|蒼真|蒼太|太一|大河|大希|暖|朝陽|文哉|湊|唯|悠生|陽輝|璃空|陸翔|琉惺|竜輝|諒|和希|凛太郎|晄|琥珀|颯汰|陽菜|結衣|葵|さくら|優奈|美優|心優|莉子|美桜|結菜|あかり|芽生|莉奈|美咲|心結|彩乃|美羽|楓|ひなた|絢音|芽依|結愛|心愛|愛莉|琴音|彩花|七海|美海|優衣|和奏|凛|こころ|愛菜|心|玲奈|ひより|彩音|咲希|真央|桃花|未来|優|凜|葉月|花|結|彩葉|菜々子|桜|心菜|心美|奈々|美結|美月|優花|澪|愛|杏奈|夏希|花音|芽衣|菜々美|小春|心咲|真緒|真由|日和|美緒|百華|優菜|優那|柚希|璃子|ひなの|ほのか|ゆい|愛奈|咲良|詩音|雫|朱音|心春|奈央|寧音|寧々|乃愛|美空|百花|舞桜|穂香|優里|悠|由奈|陽菜乃|里桜|莉愛|莉央|くるみ|愛華|愛実|茜|梓|綾乃|一花|夏帆|華|華恋|結花|結月|向日葵|彩愛|彩月|彩心|実咲|朱里|心晴|真帆|桃奈|美音|舞|穂乃花|萌衣|萌花|悠花|遥菜|陽向|梨桜|璃乃|瑠花|瑠菜|怜奈|玲|和花|莉緒)";
		String name = data.Name();
		String[] na = name.split(" ");
		
		assertTrue(na[0].matches(str1));
		assertTrue(na[1].matches(str2));
	}
	public void testMailAddress(){
		String str1 = "[A-Za-z0-9]+";
		String str2 = "(mmm|ggg|xxx|yyy|zzz)";
		String str3 = "(net|com|co.jp)";
		
		String mailadd = data.MailAddress();
		String[] username = mailadd.split("@");
		
		assertTrue(username[0].matches(str1));
		
		String[] domain = username[1].split("\\.");
		if(domain.length>3) domain[1] += "."+ domain[2];
		
		assertTrue(domain[0].matches(str2));
		assertTrue(domain[1].matches(str3));
	}
	public void testTelephoneNumber(){
		String str1 = "[0-9]+";
		String phone = data.TelephoneNumber();
		String[] p = phone.split("-");
		
		assertTrue(p[0].matches(str1));
		assertTrue(p[1].matches(str1));
		assertTrue(p[1].charAt(0)!= '0');
		assertTrue(p[2].matches(str1));
		
	}
	public void testCreateDate(){
		String u = "[A-z]+";
		String l = "[a-z]+";
		String f = "[0-9]+";
		String ul = "[A-Za-z]+";
		String uf = "[A-Z0-9]+";
		String lf = "[a-z0-9]+";
		String all = "[A-Za-z0-9]+";
		int cnt = 10;
		
		String testdata = "";
		testdata = data.CreateData("uppercase", cnt);
		assertTrue(testdata.matches(u));
		
		testdata = data.CreateData("lowercase", cnt);
		assertTrue(testdata.matches(l));
		
		testdata = data.CreateData("figure", cnt);
		assertTrue(testdata.matches(f));
		
		testdata = data.CreateData("upperlower", cnt);
		assertTrue(testdata.matches(ul));
		
		testdata = data.CreateData("upperfigure", cnt);
		assertTrue(testdata.matches(uf));
		
		testdata = data.CreateData("lowerfigure", cnt);
		assertTrue(testdata.matches(lf));
		
		testdata = data.CreateData("allcase", cnt);		
		assertTrue(testdata.matches(all));
		
		testdata = data.CreateData("address", cnt);
		assertTrue(!testdata.equals(""));
		
		testdata = data.CreateData("mailaddress", cnt);
		assertTrue(!testdata.equals(""));
		
		testdata = data.CreateData("telephonenumber", cnt);
		assertTrue(!testdata.equals(""));
			
	}
	public void testRangeofcharactersU(){
				
		assertEquals("ABC", data.RangeofcharactersU('A', 'C'));
		assertNotSame("abc", data.RangeofcharactersU('A', 'C'));		
		
	}
	public void testRangeofcharactersL(){
		
		assertEquals("abc", data.RangeofcharactersL('a', 'c'));
		assertNotSame("ABC", data.RangeofcharactersL('a', 'c'));
		
	}
	public void testRangeofcharactersF(){
		
		assertEquals("234", data.RangeofcharactersL('2', '4'));
		assertNotSame("234", data.RangeofcharactersL('1', '3'));
	}
	public void testCreateDataFree(){
		
	}
}
