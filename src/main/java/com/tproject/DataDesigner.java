package com.tproject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
/**
 * 
 * @author Oh
 *
 */

public class DataDesigner {
	private  Random ran = new Random();
	private  String[] first_name = {"大翔","樋口","悠斗","陽向","翔太","悠","颯太","悠太","翔","蓮","駿","陸","悠真","瑛太","大輝","陽斗","響","大樹","大和","陽","陸斗","一真","優","雄大","龍之介","翔大","海斗","輝","優斗","悠希","翼","琉生","樹","奏太","大雅","拓海","優翔","陽翔","健太","蒼空","太陽","大悟","琉斗","琥太郎","一輝","健斗","大地","隼","歩夢","優太","悠樹","悠翔","陽太","和真","翔真","一颯","啓太","結","航輝","蒼","大晴","大智","大輔","拓真","隼","優希","優","遥斗","涼太","怜央","颯","颯","永遠","瑛斗","空","圭","健心","虎太郎","鼓太郎","康太","航","春希","蒼真","蒼太","太一","大河","大希","暖","朝陽","文哉","湊","唯","悠生","陽輝","璃空","陸翔","琉惺","竜輝","諒","和希","凛太郎","晄","琥珀","颯汰","陽菜","結衣","葵","さくら","優奈","美優","心優","莉子","美桜","結菜","あかり","芽生","莉奈","美咲","心結","彩乃","美羽","楓","ひなた","絢音","芽依","結愛","心愛","愛莉","琴音","彩花","七海","美海","優衣","和奏","凛","こころ","愛菜","心","玲奈","ひより","彩音","咲希","真央","桃花","未来","優","凜","葉月","花","結","彩葉","菜々子","桜","心菜","心美","奈々","美結","美月","優花","澪","愛","杏奈","夏希","花音","芽衣","菜々美","小春","心咲","真緒","真由","日和","美緒","百華","優菜","優那","柚希","璃子","ひなの","ほのか","ゆい","愛奈","咲良","詩音","雫","朱音","心春","奈央","寧音","寧々","乃愛","美空","百花","舞桜","穂香","優里","悠","由奈","陽菜乃","里桜","莉愛","莉央","くるみ","愛華","愛実","茜","梓","綾乃","一花","夏帆","華","華恋","結花","結月","向日葵","彩愛","彩月","彩心","実咲","朱里","心晴","真帆","桃奈","美音","舞","穂乃花","萌衣","萌花","悠花","遥菜","陽向","梨桜","璃乃","瑠花","瑠菜","怜奈","玲","和花","莉緒"}; 	 	
	private  String[] family_name = {"饗庭","青木","青山","赤木","赤坂","明石","赤松","秋山","明智","浅野","朝日","蘆田","麻生","足利","阿多","安室","荒川","有田","有馬","愛子","飯尾","飯島","飯田","飯沼","生田","井口","池田","石川","石垣","一色","石塔","石野","石橋","石谷","泉","泉出","磯野","板倉","伊那","井上","猪子","今川","入野屋","岩淵","岩間","岩松","植田","上地","植村","上野","上野山","上原","魚往","宇野","浦野","江川","江見","及川","大池","大木","大河内","大島","大崎","太田","大竹","大窪","大寺","大野","大森","大山","岡本","奥","奥田","奥平","小河","小国","小椋","尾崎","小鹿","小田","落合","小原","小俣","恩田","各務","蔭山","加古","柏原","春日部","片切","金井","金森","可児","蟹沢","釜内","神出","上条","上山","川島","河辺","神吉","蒲原","岸本","木田","喜連川","衣笠","吉良","櫛田","櫛橋","久保","黒川","小泉","上月","小島","小寺","小林","榊原","真田","佐用","沢井","沢田","志岐","重田","七条","品川","斯波","渋川","島田","下山","清水","志和","神野","新免","須賀","菅井","菅名","菅沼","杉田","諏訪部","瀬尾","関口","瀬名","高島","高楡","鷹巣","瀧沢","田島","多治見","高井","高木","高田","孝橋","高屋","高山","竹田","竹中","多田","楯岡","田中","谷地","田部井","垂木","知久","津川","辻","土屋","土山","堤","豊島","手島","手塚","寺井","天童","東","遠山","戸賀崎","戸坂","戸崎","土岐","得平","富塚","外山","豊田","長岡","中川","長沢","中島","中野","永野","中村","中山","名倉","夏目","成瀬","新野（入野）","仁木","西","西尾","二本松","丹羽","能勢","野中","萩原","畠山","蜂須賀","蜂屋","羽仁","花房","馬場","原","林田","葉山","東根","彦坂","土方","肥田","平井","平岡","平田","平野","広岡","広沢","広瀬","福島","福田","福知","福原","船木","不破","別所","穂積","細川","堀","堀越","本郷","前川","間島","松岡","松倉","松本","三木","三刀屋","水上","水田","水野","向井","村井","村田","村山","持永","桃井","森","安井","矢田","柳","矢吹","山県","山田","山野辺","吉野","依田","和賀","若林","脇田"};
	private	 String[] address = {"北海道","青森県","岩手県","宮城県","秋田県","山形県","福島県","茨城県","栃木県","群馬県","埼玉県","千葉県","東京都","神奈川県","新潟県","富山県","石川県","福井県","山梨県","長野県","岐阜県","静岡県","愛知県","三重県","滋賀県","京都府","大阪府","兵庫県","奈良県","和歌山県","鳥取県","島根県","岡山県","広島県","山口県","徳島県","香川県","愛媛県","高知県","福岡県","佐賀県","長崎県","熊本県","大分県","宮崎県","鹿児島県","沖縄県"};
	DataDesigner(){

	}
	public String Uppercase(){
		int r = ran.nextInt(26);
		int u = 65 + r;
		
		return String.valueOf((char)u);
	}
	public String Lowercase(){
		int r = ran.nextInt(26);
		int l = 97 + r ;
		
		return String.valueOf((char)l);
	}
	public int Figure(){
		return ran.nextInt(10);
	}
	public String Upperlower(){
		return RandomStringUtils.randomAlphabetic(1);
	}
	public String Upperfigure(){
		int uf = ran.nextInt(2);
		String str = "";
		return uf == 0 ? str+Uppercase() : String.valueOf(Figure()); 
	}
	public String Lowerfigure(){
		int lf = ran.nextInt(2);
		String str = "";
		return lf == 0 ? str+Lowercase() : String.valueOf(Figure());
	}
	public String Allcase(){
		return RandomStringUtils.randomAlphanumeric(1);
	}
	public String Address(){
		int add = ran.nextInt(address.length-1);
		
		return address[add];
	}
	public String Name(){
		int first = ran.nextInt(first_name.length-1);
		int family = ran.nextInt(family_name.length-1);
		return family_name[family]+" "+first_name[first];
	}
	public String MailAddress(){
		String[] mail = {".com",".net",".co.jp"};
		String[] maildomain = {"mmm","ggg","xxx","yyy","zzz"};
		int i = ran.nextInt(maildomain.length-1);
		int j = ran.nextInt(mail.length-1);
		String str =RandomStringUtils.random(RandomUtils.nextInt(10)+1, true, true)+"@"+maildomain[i]+mail[j];
	
		return str;
	}
	public String TelephoneNumber(){
		String phone ="";
		phone = "0" + RandomStringUtils.random(RandomUtils.nextInt(2)+1, "123456789")+ "-" 
				+ RandomStringUtils.random(1, "123456789") + RandomStringUtils.randomNumeric(RandomUtils.nextInt(2)+2) + "-"
				+ RandomStringUtils.randomNumeric(4);

		return phone;
	}	
	/**
	 * 新しいデータを生成
	 * @param stm 生成するデータの種類
	 * @param cnt　生成するデータの文字数
	 * @return　生成したデータ
	 */
	public String CreateData(String stm, int cnt){
		String s="";
		
		if(stm.equals("uppercase")){
			for(int i=0;i<cnt;i++){
				s = s + this.Uppercase();
			}
			return s;
		}else if(stm.equals("lowercase")){
			for(int j=0;j<cnt;j++){
				s = s + this.Lowercase();
			}
			return s;		
		}else if(stm.equals("upperlower")){
			for(int j=0;j<cnt;j++){
				s = s + this.Upperlower();
			}
		}else if(stm.equals("upperfigure")){
			for(int j=0;j<cnt;j++){
				s = s + this.Upperfigure();
			}
		}else if(stm.equals("lowerfigure")){
			for(int j=0;j<cnt;j++){
				s = s +  this.Lowerfigure();
			}
		}else if(stm.equals("allcase")){
			for(int j=0;j<cnt;j++){
				s = s +  this.Allcase();
			}		
		}else if(stm.equals("address")){
			s = this.Address();
		}else if(stm.equals("mailaddress")){
			s = this.MailAddress();
		}else if(stm.equals("name")){
			s = this.Name();
		}else if(stm.equals("telephonenumber")){
			s = this.TelephoneNumber();
		}else{
			for(int k=0;k<cnt;k++){
				s = s + String.valueOf(this.Figure());
			}
		}
		return s;
	}
	public String RangeofcharactersU(char start,char end){
		
		int s = (int)start;
		int e = (int)end;		
		int length = e - s + 1;
		String str="";
		for(int i=0;i<length;i++){
			char l =(char)(s + i) ;
			str = str + l;			
		}
		return str;
	}
	public String RangeofcharactersL(char start,char end){
		int s = (int)start;
		int e = (int)end;
		int length = e - s + 1;
		String str="";
		for(int i=0;i<length;i++){
			char l =(char)(s + i) ;
			str = str + l;			
		}
		return str;
	}
	public String RangeofcharactersF(char start,char end){
		int s = (int)start-48;
		int e = (int)end-48;
		String str="";
		for(int i=s;i<=e;i++){
			str = str + String.valueOf(i);			
		}
		return str;
	}
	public String CreateDataFree(DataFormatSelectionPanel dataformat, int datalength){
		String s="";
		for(int i=0;i<dataformat.length;i++){
			if(dataformat.dataformat[i] == null || dataformat.dataformat[i].equals("")) break;
			if(dataformat.dataformat[i].equals("upper")){
				s = s + this.Uppercase();
			}else if(dataformat.dataformat[i].equals("lower")){
				s = s + this.Lowercase();
			}else if(dataformat.dataformat[i].equals("figure")){
				int figure = this.Figure();
				s = s + String.valueOf(figure);
			}else if(dataformat.dataformat[i].equals("upperlower")){
				String upperlower = this.Upperlower();
				s = s + upperlower;
			}else if(dataformat.dataformat[i].equals("upperfigure")){
				String upperfigure = this.Upperfigure();
				s = s + upperfigure;
			}else if(dataformat.dataformat[i].equals("lowerfigure")){
				String lowerfigure = this.Lowerfigure();
				s = s + lowerfigure;
			}else if(dataformat.dataformat[i].equals("allcase")){
				String allcase = this.Allcase();
				s = s + allcase;
			}else if(dataformat.dataformat[i].equals("RangeC")){
				String range = "";
				if(dataformat.rangeC1T != null && dataformat.rangeC2T != null){
					String rangeU = this.RangeofcharactersU(dataformat.datatext[i].charAt(1), dataformat.datatext[i].charAt(3));
					range = range + RandomStringUtils.random(1, rangeU);
					if(dataformat.rangeC3T != null && dataformat.rangeC4T != null){
						String rangeL = this.RangeofcharactersL(dataformat.datatext[i].charAt(5), dataformat.datatext[i].charAt(7));
						range = range + RandomStringUtils.random(1, rangeL);
						if(dataformat.rangeC5T !=null && dataformat.rangeC6T != null){
							String rangeF = this.RangeofcharactersF(dataformat.datatext[i].charAt(9), dataformat.datatext[i].charAt(11));
							range = range + RandomStringUtils.random(1, rangeF);
						}else{
							
						}
					}else{
						if(dataformat.rangeC5T !=null && dataformat.rangeC6T != null){
							String rangeF = this.RangeofcharactersF(dataformat.datatext[i].charAt(7), dataformat.datatext[i].charAt(9));
							range = range + RandomStringUtils.random(1, rangeF);
						}						
					}
				}else{
					if(dataformat.rangeC3T != null && dataformat.rangeC4T != null){
						String rangeL = this.RangeofcharactersL(dataformat.datatext[i].charAt(3), dataformat.datatext[i].charAt(5));
						range = range + RandomStringUtils.random(1, rangeL);
						if(dataformat.rangeC5T !=null && dataformat.rangeC6T != null){
							String rangeF = this.RangeofcharactersF(dataformat.datatext[i].charAt(7), dataformat.datatext[i].charAt(9));
							range = range + RandomStringUtils.random(1, rangeF);
						}
					}else{
						if(dataformat.rangeC5T !=null && dataformat.rangeC6T != null){
							String rangeF = this.RangeofcharactersF(dataformat.datatext[i].charAt(5), dataformat.datatext[i].charAt(7));
							range = range + RandomStringUtils.random(1, rangeF);
						}
					}
				}	
				s = s + RandomStringUtils.random(1, range);
			}else if(dataformat.dataformat[i].equals("RangeN")){
				if(dataformat.rangeN1T !=null && dataformat.rangeN2T != null){
					String rangeN = this.RangeofcharactersF(dataformat.datatext[i].charAt(1), dataformat.datatext[i].charAt(3));

					s = s + RandomStringUtils.random(1, rangeN);
				}else{
					
				}			
			}else if(dataformat.dataformat[i].equals("Assignment")){
				for(int m=0;m<100;m++){
					String assign = RandomStringUtils.random(1,dataformat.datatext[i]);
					if(!assign.equals("[")){
						if(!assign.equals("]")){
							s = s + assign; 
							break;
						}
					}
				}
			}else{
				s = s + dataformat.datatext[i];
			}
		}
		return s;		
	}
	
	public String[] Numberdatagenerated(String stm,int cnt,int num){
		String[] str= new String[num];
		for(int i=0;i<num;i++){
			str[i]=this.CreateData(stm, cnt);
		}
		return str;		
	}
	public String Columnseleted(ResultStruct r){
		int index = ran.nextInt(r.getResult().size());
		String data = r.getResult().get(index).getDataList().get(0).toString();
		for(int i=1;i<r.getResult().get(index).getDataList().size();i++){
			data += "','"+r.getResult().get(index).getDataList().get(i).toString();
		}
		return data;
	}
	@SuppressWarnings("deprecation")
	public String createDate(String startYear, String startMonth, String startDay, String stopYear, String stopMonth, String stopDay) {
		int y = Integer.parseInt(startYear)-1900;
		if(startMonth.equals("")) startMonth = "1";
		int m = Integer.parseInt(startMonth);
		int d = Integer.parseInt(startDay);
		int y1 = Integer.parseInt(stopYear)-1900;
		int m1 = Integer.parseInt(stopMonth);
		int d1 = Integer.parseInt(stopDay);
		boolean flag = false;
	
		Date start = new Date(y,m,d);
		Date end = new Date(y1,m1,d1);
		Date createDate = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy'年'MM'月'dd'日'");
	    sdf1.applyPattern("yyyy-MM-dd"); 
	    
	    int r_y;
	    int r_m;
	    int r_d;
	    while(!flag){
	    	r_y = y + ran.nextInt(y1-y);
		    r_m = ran.nextInt(12);
		    r_d = r_m != 2 ? ran.nextInt(30)+1 : ran.nextInt(28)+1;
		    createDate.setYear(r_y);
		    createDate.setMonth(r_m);
		    createDate.setDate(r_d);
	    	
		    if(start.before(createDate) && end.after(createDate)){
	    		flag = true;	    		
	    	}	    	
	    }
	    	    
		return String.valueOf(sdf1.format(createDate));
	}
	
}
