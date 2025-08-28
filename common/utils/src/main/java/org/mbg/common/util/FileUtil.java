package org.mbg.common.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serial;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

/**
 *
 * @author linhlh2
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {

    public static final String ATTACHMENT = "attachment;filename=";

    public static final String DOC = "doc";

    public static final String DOCX = "docx";

//    public static final String FILE_SPLIT_PARTERN = "\\.(?=[^\\.]+$)";

    public static final String GIF = "gif";

    public static final String HEIC = "heic";

    public static final String INLINE = "inline;filename=";

    public static final String JPEG = "jpeg";

    public static final String JPG = "jpg";

    public static final String PDF = "pdf";

    public static final String PNG = "png";

    public static final String PPT = "ppt";

    public static final String PPTX = "pptx";

    public static final String XLS = "xls";

    public static final String XLSX = "xlsx";

    private static final Map<String, String> FILE_TYPE_MAP = new HashMap<>() {
        @Serial
        private static final long serialVersionUID = 6144887420160967657L;

        {
            put(PNG, "image/png");
            put(DOC, "application/msword");
            put(DOCX, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            put(PDF, "application/pdf");
            put(JPG, "image/jpeg");
            put(JPEG, "image/jpeg");
            put(GIF, "image/gif");
            put(XLS, "application/vnd.ms-excel");
            put(XLSX, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            put(PPT, "application/vnd.ms-powerpoint");
            put(PPTX, "application/vnd.openxmlformats-officedocument.presentationml.presentation");
            put(HEIC, "image/heic");
        }
    };

    public static BufferedImage decodeToImage(String imageString) {
        BufferedImage image = null;

        byte[] imageByte;

        try {
            imageByte = Base64.decodeBase64(imageString);

            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);

            image = ImageIO.read(bis);

            bis.close();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return image;
    }

    /**
     * Encode image to string
     *
     * @param image The image to encode
     * @param type jpeg, bmp, ...
     * @return encoded string
     */
    public static String encodeToString(BufferedImage image, String type) {
        String imgStr = StringPool.BLANK;

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ImageIO.write(image, type, bos);

            byte[] imageBytes = bos.toByteArray();

            imgStr = Base64.encodeBase64String(imageBytes);
        } catch (IOException ioe) {
            _log.error(ioe.getMessage(), ioe);
        }

        return imgStr;
    }

    public static BufferedImage getBufferedImage(Blob blob) {
        if (blob == null) {
            return null;
        }

        BufferedImage buffered = null;

        try {
            InputStream inStream = blob.getBinaryStream();

            buffered = ImageIO.read(inStream);
        } catch (SQLException | IOException sqlex) {
            _log.error(sqlex.getMessage(), sqlex);
        }

        return buffered;
    }

    public static String getContentType(String extention) {
        return FILE_TYPE_MAP.get(extention);
    }

    public static String getFileChecksum(MessageDigest digest, byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        byte[] mdbytes = digest.digest(bytes);

        for (byte mdbyte : mdbytes) {
            sb.append(Integer.toString((mdbyte & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static String getFileExtension(String fileName) {
        if (Validator.isNull(fileName)) {
            return null;
        }

        return Files.getFileExtension(fileName);
    }

    public static String getFileName(String fileName) {
        if (Validator.isNull(fileName)) {
            return null;
        }

        return Files.getNameWithoutExtension(fileName);
    }

    public static String getFileNameTimestamp(String fileName) {
        if (Validator.isNull(fileName)) {
            return null;
        }

        String fileNamePart = getFileName(fileName);
        String extPart = getFileExtension(fileName);

        return fileNamePart +
                StringPool.UNDERLINE +
                DateUtil.formatStringLongTimestamp(new Date()) +
                StringPool.PERIOD +
                extPart;
    }

    public static String getFileSHA256Checksum(byte[] bytes) {
        String checkSum = StringPool.BLANK;

        try {
            MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");

            checkSum = getFileChecksum(sha256Digest, bytes);
        } catch (NoSuchAlgorithmException nsae) {
            _log.error(nsae.getMessage(), nsae);
        }

        return checkSum;
    }

    public static String getImageBase64String(Blob blob, String type) {
        String imgStr = StringPool.BLANK;

        try {
            InputStream inStream = blob.getBinaryStream();

            imgStr = getImageBase64String(inStream, type);
        } catch (SQLException sqlex) {
            _log.error(sqlex.getMessage(), sqlex);
        }

        return imgStr;
    }

    public static String getImageBase64String(InputStream inStream, String type) {
        String imgStr = StringPool.BLANK;

        try {
            BufferedImage img = ImageIO.read(inStream);

            imgStr = encodeToString(img, type);
        } catch (IOException ioe) {
            _log.error(ioe.getMessage(), ioe);
        }

        return imgStr;
    }

    public static String getImageBase64String(String url, String type) {
        String imgStr = StringPool.BLANK;

        try {
            BufferedImage img = ImageIO.read(new File(url));

            imgStr = encodeToString(img, type);
        } catch (IOException ioe) {
            _log.error(ioe.getMessage(), ioe);
        }

        return imgStr;
    }

    public static String getImageSrcBase64String(Blob blob, String type) {

        return "data:image" +
                StringPool.SLASH +
                type +
                ";base64," +
                getImageBase64String(blob, type);
    }

    public static String getImageSrcBase64String(BufferedImage image, String type) {

        return "data:image" +
                StringPool.SLASH +
                type +
                ";base64," +
                encodeToString(image, type);
    }

    public static String getImageSrcBase64String(InputStream inStream, String type) {

        return "data:image" +
                StringPool.SLASH +
                type +
                ";base64," +
                getImageBase64String(inStream, type);
    }

    public static String getImageSrcBase64String(String url, String type) {

        return "data:image" +
                StringPool.SLASH +
                type +
                ";base64," +
                getImageBase64String(url, type);
    }

    public static Long getKilobyte(Long sizeB) {
        return sizeB / (1024);
    }

    public static Long getMegabyte(Long sizeB) {
        return getKilobyte(sizeB) / 1024;
    }

    public static String getNormalizeName(String fileName) {
        if (Validator.isNull(fileName)) {
            return null;
        }

        fileName = AccentRemoverUtil.removeAccent(fileName).toLowerCase();

        String fileNamePart = getFileName(fileName);
        String extPart = getFileExtension(fileName);

        String sb = fileNamePart +
                StringPool.DASH +
                DateUtil.formatStringShortTimestamp(new Date()) +
                StringPool.PERIOD +
                extPart;

        return StringUtil.replace(StringUtil.trimAll(sb), StringPool.SPACE, StringPool.DASH);
    }

    public static String getOrCreateFolder(String rootDirName, String newDirName) {
        StringBuilder sb = new StringBuilder();

        sb.append(rootDirName);

        if (newDirName != null) {
            sb.append(StringPool.SLASH);
            sb.append(newDirName);
        }

        File newDir = new File(sb.toString());

        if (!newDir.exists() && !newDir.mkdirs()) {
            _log.warn("Cannot create new folder: {}", newDirName);

            return null;
        }

        if (!newDir.isDirectory() && (!newDir.delete() || !newDir.mkdir())) {
            _log.warn("Cannot create new folder, the target is not a folder: {}", newDirName);

            return null;
        }

        return sb.toString();
    }

    public static String getSafeFileName(String input) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c != '/' && c != '\\' && c != 0) {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static String getTextFromBlob(Blob blob) {
        String text = StringPool.BLANK;

        try {
            if (blob != null) {
                InputStream inStream = blob.getBinaryStream();

                StringWriter writer = new StringWriter();

                IOUtils.copy(inStream, writer, StringPool.UTF8);

                text = writer.toString();
            }
        } catch (SQLException | IOException fnfe) {
            _log.error(fnfe.getMessage(), fnfe);
        }

        return text;
    }

    public static boolean isValidFileExtension(String extension, List<String> allowExts) {

        return allowExts.contains(extension.toLowerCase());
    }

    public static boolean isValidFileExtension(String extension, String[] allowExts) {
        List<String> exts = Arrays.asList(allowExts);

        return isValidFileExtension(extension, exts);
    }

    public static boolean isValidMaxSize(int size, Long maxSize) {
        if (Validator.isNull(maxSize)) {
            return true;
        }

        return size <= maxSize.intValue();
    }

    public static BufferedImage resizeImage(BufferedImage bimg, int targetWidth, int targetHeight) {
        int width = bimg.getWidth();
        int height = bimg.getHeight();

        if (width == targetWidth && height == targetHeight) {
            return bimg;
        }

        byte[] data = resizeImageByte(bimg, targetWidth, targetHeight);

        if (data.length == 0) {
            return null;
        }

        BufferedImage bi = null;

        try (ByteArrayInputStream is = new ByteArrayInputStream(data)) {
            bi = ImageIO.read(is);
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return bi;
    }

    public static byte[] resizeImageByte(BufferedImage bimg, int targetWidth, int targetHeight) {

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            int width = bimg.getWidth();
            int height = bimg.getHeight();

            if (width == targetWidth && height == targetHeight) {
                ImageIO.write(bimg, JPEG, bos);
            } else {
                Thumbnails.of(bimg)
                                .forceSize(targetWidth, targetHeight)
                                .outputFormat(JPEG)
                                .outputQuality(1)
                                .toOutputStream(bos);
            }

            return bos.toByteArray();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return new byte[0];
    }

    public static String sanitizeFilename(String input) {
        String[] invalidSymbols = {
                StringPool.BACK_SLASH,
                StringPool.SLASH,
                StringPool.COLON,
                StringPool.STAR,
                StringPool.QUOTE,
                StringPool.LESS_THAN,
                StringPool.GREATER_THAN,
                StringPool.PIPE,
                StringPool.OPEN_PARENTHESIS,
                StringPool.CLOSE_PARENTHESIS,
                StringPool.AMPERSAND
        };

        for (String currentSymbol : invalidSymbols) {
            input = input.replaceAll( StringPool.OPEN_BRACKET + StringPool.BACK_SLASH + StringPool.CLOSE_BRACKET ,
                    StringPool.UNDERLINE);
        }

        return input;
    }

    public static boolean write(byte[] data, String dirPath, String fileName) {
        boolean success = false;

        try (FileOutputStream out = new FileOutputStream(dirPath + StringPool.SLASH + fileName)) {
            out.write(data);

            success = true;
        } catch (Exception ex) {
            _log.error("Cannot create file: " + fileName);
        }

        return success;
    }

    public static void write(InputStream in, File file) {

        try (OutputStream out = new FileOutputStream(file)) {
            int read;

            byte[] bytes = new byte[5120];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
        } catch (IOException fnfe) {
            _log.error(fnfe.getMessage(), fnfe);
        }
    }

    public static boolean write(MultipartFile file, String dirPath, String fileName) {
        boolean success = false;

        try (FileOutputStream out = new FileOutputStream(dirPath + StringPool.SLASH + fileName)) {

            out.write(file.getBytes());

            success = true;
        } catch (Exception ex) {
            _log.error("Cannot create file: " + fileName);
        }

        return success;
    }
}
