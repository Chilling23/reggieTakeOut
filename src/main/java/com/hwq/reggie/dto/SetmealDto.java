package com.hwq.reggie.dto;

import com.hwq.reggie.entity.Setmeal;
import com.hwq.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
