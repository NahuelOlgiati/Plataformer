package com.mandarina.lvlbuilder.feature;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.w3c.dom.NodeList;

import com.mandarina.lvlbuilder.LvlBuilderImage;
import com.mandarina.lvlbuilder.RGB;

public class PNGMetadataUtil {

	public static void readFeatures(LvlBuilderImage img, PNGMetadata pm) {
		try (ImageInputStream input = ImageIO.createImageInputStream(img.getResource().getFile())) {
			Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
			ImageReader reader = readers.next();
			reader.setInput(input);
			IIOMetadata metadata = reader.getImageMetadata(0);
			for (TileFeature tf : TileFeature.values()) {
				for (RGB rgb : RGB.values()) {
					String key = PNGMetadata.getKey(rgb, tf.getKeyCode());
					String textEntry = getTextEntry(metadata, key);
					if (textEntry != null && !textEntry.isEmpty()) {
						Object object = tf.getManager().fromString(pm, rgb, textEntry);
						pm.add(rgb, tf.getKeyCode(), object);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeMetadata(File file, PNGMetadata pm) {
		try (ImageInputStream input = ImageIO.createImageInputStream(file);
				ImageOutputStream output = ImageIO.createImageOutputStream(file)) {
			Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
			ImageReader reader = readers.next();
			reader.setInput(input);
			IIOImage image = reader.readAll(0, null);
			IIOMetadata metadata = image.getMetadata();
			clearMetadata(metadata);
			for (TileFeature tf : TileFeature.values()) {
				for (RGB rgb : RGB.values()) {
					String textEntry = tf.getManager().toString(pm, rgb);
					if (textEntry != null && !textEntry.isEmpty()) {
						String key = PNGMetadata.getKey(rgb, tf.getKeyCode());
						addTextEntry(metadata, key, textEntry);
					}
				}
			}
			ImageWriter writer = ImageIO.getImageWriter(reader);
			writer.setOutput(output);
			writer.write(image);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void log(PNGMetadata pm) {
		for (TileFeature tf : TileFeature.values()) {
			for (RGB rgb : RGB.values()) {
				String textEntry = tf.getManager().toString(pm, rgb);
				if (textEntry != null && !textEntry.isEmpty()) {
					String key = PNGMetadata.getKey(rgb, tf.getKeyCode());
					System.out.println(key);
					System.out.println(textEntry);
					System.out.println();
				}
			}
		}
	}

	private static void clearMetadata(final IIOMetadata metadata) throws IIOInvalidTreeException {
		metadata.setFromTree(IIOMetadataFormatImpl.standardMetadataFormatName,
				new IIOMetadataNode(IIOMetadataFormatImpl.standardMetadataFormatName));
	}

	private static void addTextEntry(final IIOMetadata metadata, final String key, final String value)
			throws IIOInvalidTreeException {
		IIOMetadataNode text = getTextEntry(key, value);
		IIOMetadataNode root = new IIOMetadataNode(IIOMetadataFormatImpl.standardMetadataFormatName);
		root.appendChild(text);
		metadata.mergeTree(IIOMetadataFormatImpl.standardMetadataFormatName, root);
	}

	private static IIOMetadataNode getTextEntry(final String key, final String value) {
		IIOMetadataNode textEntry = new IIOMetadataNode("TextEntry");
		textEntry.setAttribute("keyword", key);
		textEntry.setAttribute("value", value);
		IIOMetadataNode text = new IIOMetadataNode("Text");
		text.appendChild(textEntry);
		return text;
	}

	private static String getTextEntry(final IIOMetadata metadata, final String key) {
		IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);
		NodeList entries = root.getElementsByTagName("TextEntry");
		for (int i = 0; i < entries.getLength(); i++) {
			IIOMetadataNode node = (IIOMetadataNode) entries.item(i);
			if (node.getAttribute("keyword").equals(key)) {
				return node.getAttribute("value");
			}
		}
		return null;
	}
}