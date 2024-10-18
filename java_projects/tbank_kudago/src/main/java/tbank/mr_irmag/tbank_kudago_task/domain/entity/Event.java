package tbank.mr_irmag.tbank_kudago_task.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private Long id;
    private List<EventDate> dates;

    private String title;

    private String price;

    public double getValueFromPrice(){
        if(this.price.isBlank()) {
            return 0.0;
        } else {
            var parts = price.split(" ");
            if(this.price.contains("бесплатный")) {
                return 0.0;

            } else if(this.price.contains("от")){
                return Double.parseDouble(parts[1]);

            } else if(this.price.contains("взрослый билет")) {
                String regex = "взрослый билет – (\\d+) рублей";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(this.price);

                if (matcher.find()) {
                    return Double.parseDouble(matcher.group(1));
                } else {
                    return 0.0;
                }
            }  else {
                return Double.parseDouble(parts[0]);
            }
        }

    }
}
