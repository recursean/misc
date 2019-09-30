import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

public class HammingCode{
    public int[] bitMask = {0x00,0x80,0x40,0x20,0x10,0x08,0x04,0x02,0x01}; 
    public HammingCode(){

    }

    public HammingCode(FileInputStream dataFile){
	try{
	    encode(dataFile);
	    decode(new FileInputStream("Encoded.txt"));
	}catch(FileNotFoundException e){};
    }

    private void encode(FileInputStream input) throws FileNotFoundException{
	FileOutputStream fos = new FileOutputStream("Encoded.txt");
	int[] H = {0,0x1E,0x2D,0x33,0x4B,0x55,0x66,0x78,0x87,
		       0x99,0xAA,0xB4,0xCC,0xD2,0xE1,0xFF};
	int avail = 0;
	try{
	    avail = input.available();
	}catch(IOException e){}
	
	for(int i = 0; i < avail; i++)
	    try{
		int bitStr = input.read();
		int bitMask = 0x0F;
		fos.write((H[bitStr >>> 4]));
		fos.write((H[bitMask & bitStr]));
	    }
	    catch(IOException e){}
	try{
	    fos.flush();
	    fos.close();
	}catch(IOException e){}
    }

    private void decode(FileInputStream encoded) throws FileNotFoundException{
	FileOutputStream fos = new FileOutputStream("Decoded.txt");
	int avail = 0,enc = 0,bitStr1,bitStr2,dec,rand;
	Random r = new Random();
	try{
	    avail = encoded.available();
	}catch(IOException e){}
	try{
	    
	    for(int i = 0; i < avail; i ++){
		enc = encoded.read();
		if(enc == -1)
		    break;

		//testing...
		rand = r.nextInt(8);
		enc = enc ^ bitMask[rand];
		
		enc = errDetect(enc);
		
		if(enc == -1)
		    break;
		bitStr1 = (enc >>> 4);
		bitStr1 = (bitStr1 << 4);
		
		//testing...
	        rand = r.nextInt(8);
		enc = enc ^ bitMask[rand];
				
		enc = encoded.read();
		enc = errDetect(enc);
		
		bitStr2 = (enc >>> 4);
		
		dec = (bitStr1 | bitStr2);

		fos.write(dec);
	    }
	}catch(IOException e){}
	
	try{
	    fos.flush();
	    fos.close();
	}catch(IOException e){}
    }

    private int errDetect(int bitStr){
	int b1,b2,b3,b4,b5,b6,b7,b8,s1,s2,s3,s4,sfin,subSFin;

	b1 = (bitMask[1] & bitStr) >>> 7;
	b2 = (bitMask[2] & bitStr) >>> 6;
	b3 = (bitMask[3] & bitStr) >>> 5;
	b4 = (bitMask[4] & bitStr) >>> 4;
	b5 = (bitMask[5] & bitStr) >>> 3;
	b6 = (bitMask[6] & bitStr) >>> 2;
	b7 = (bitMask[7] & bitStr) >>> 1;
	b8 = (bitMask[8] & bitStr);

	s1 = b4 ^ b5 ^ b6 ^ b7;
	s2 = b2 ^ b3 ^ b6 ^ b7;
	s3 = b1 ^ b3 ^ b5 ^ b7;
	s4 = b1 ^ b2 ^ b3 ^ b4 ^ b5 ^ b6 ^ b7 ^ b8;

	subSFin = (s1 << 2) ^ (s2 << 1) ^ s3;
	sfin = (s1 << 3) ^ (s2 << 2) ^ (s3 << 1) ^ s4;

	if(sfin == 0)
	    return bitStr;
	else if(s4 == 1){
	    //System.out.println("FIXING 1 ERROR...");
	    if(subSFin == 0){
		bitStr = bitStr ^ 0x01;
	    }
	    else{
		bitStr = bitStr ^ bitMask[subSFin];
	    }
	}
	else if((s4 == 0) && (subSFin != 0)){
	  System.out.println("TWO ERRORS DETECTED");
	}
	
	return bitStr;
    }
    public static void main(String[] args){
	HammingCode hc;
	if(args.length > 0)
	    try{
		hc = new HammingCode(new FileInputStream(args[0]));
	    }
	    catch(FileNotFoundException e){}
    }
}
