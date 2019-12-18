package com.svintsov.instrument.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * CommandLineOptions.
 *
 * @author Ilya_Svintsov
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CommandLineOptions {

    private String fileFullPath;
    private Integer blockSize;

}
