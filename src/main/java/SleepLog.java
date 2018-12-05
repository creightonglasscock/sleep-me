    private String cCycle(){
        if(hours == null) fromStringHoursArr();
		if(numDays == 1) return 100 + "%";
        int difference = 0;
        double cperc;
        for (int k = 0; k<numDays; k++){
            for(int a = 0; a<numDays; a++) {
                for (int i = 0; i < 2; i++) {
                    int hour1 = (hours[k][i]) / 100;
                    int hour2 = (hours[a][i]) / 100;
                    if(hour1<(hour2+2) && hour1>(hour2-2)) {
                        difference += 1;
                    }else if(hour1<(hour2+4) && hour1>(hour2-4)){
                        difference +=2;
                    }else{
                        difference +=4;
                    }
                }
            }
        }
		DecimalFormat df = new DecimalFormat("##.00");
        int rec = numDays*numDays;
        int total = (difference - rec)*2;
        if(total>0){
            cperc = 100-total;
        }else{
			cperc = 100-(Math.abs(total));
		}
        df.format(cperc);
        return cperc +"%";
    }
