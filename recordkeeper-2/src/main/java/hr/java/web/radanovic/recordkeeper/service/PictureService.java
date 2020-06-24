package hr.java.web.radanovic.recordkeeper.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import hr.java.web.radanovic.recordkeeper.exception.FileException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PictureService {

	private final String path = "src/main/resources/static/img/";

	public void saveOrRepolacePic(MultipartFile file, String username) {
		File outFile = new File(path + username + ".jpg");
		if (picExists(username) && file.getSize() > 0) {
			outFile.delete();
		}
		List<String> pictureType = Arrays.asList(file.getContentType().split("/"));

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(ImageIO.read(file.getInputStream()), pictureType.get(pictureType.size() - 1), os);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (InputStream in = new ByteArrayInputStream(os.toByteArray());
				OutputStream out = new FileOutputStream(outFile);) {
			FileCopyUtils.copy(in, out);
		} catch (IOException e) {
			throw new FileException("error writing to file for user " + username);
		}
	}

	public ResponseEntity<Resource> picUrl(String username) {
		if (!picExists(username) || username == null) {
			File file = new File(path + "placeholder" + ".jpg");
			return ResponseEntity.ok().contentLength(file.length()).contentType(MediaType.parseMediaType("image/jpg"))
					.body(new FileSystemResource(file));
		}
		File file = new File(path + username + ".jpg");
		return ResponseEntity.ok().contentLength(file.length()).contentType(MediaType.parseMediaType("image/jpg"))
				.body(new FileSystemResource(file));
//		return ResponseEntity.ok().body(new FileSystemResource(file));

	}

	private boolean picExists(String username) {
		List<String> picList = Arrays.asList(new File(path).listFiles()).stream()
				.filter(e -> e.getName().startsWith(username)).map(e -> e.getName()).collect(Collectors.toList());
		if (picList.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

}
