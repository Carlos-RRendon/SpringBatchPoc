FROM ubuntu:24.10

ENV DEVIAN_FRONTEND=noninteractive

#Download dependencies
RUN apt-get update && \
    apt-get install -y openssh-server nano && \
    apt clean

#Configure sftp user
RUN useradd -m -d /home/sftpuser -s /usr/sbin/nologin sftpuser && \
    mkdir -p /home/sftpuser/.ssh && \
    chown sftpuser:sftpuser /home/sftpuser/.ssh && \
    chmod 700 /home/sftpuser/.ssh

#Copy the public key
COPY sftpkey.pub /home/sftpuser/.ssh/authorized_keys

#Set permisions for the public key
RUN chmod 600 /home/sftpuser/.ssh/authorized_keys && \
    chown sftpuser:sftpuser /home/sftpuser/.ssh/authorized_keys

# Create a directory for SFTP that the user will have access to
RUN mkdir -p /home/sftpuser/sftp/upload && \
    chown root:root /home/sftpuser /home/sftpuser/sftp && \
    chmod 755 /home/sftpuser /home/sftpuser/sftp && \
    chown sftpuser:sftpuser /home/sftpuser/sftp/upload && \
    chmod 755 /home/sftpuser/sftp/upload

# Configure SSH for SFTP
RUN mkdir -p /run/sshd && \
    echo "Match User sftpuser" >> /etc/ssh/sshd_config && \
    echo "    ChrootDirectory /home/sftpuser/sftp" >> /etc/ssh/sshd_config && \
    echo "    ForceCommand internal-sftp" >> /etc/ssh/sshd_config && \
    echo "    PasswordAuthentication no" >> /etc/ssh/sshd_config && \
    echo "    PubkeyAuthentication yes" >> /etc/ssh/sshd_config && \
    echo "    PermitTunnel no" >> /etc/ssh/sshd_config && \
    echo "    AllowAgentForwarding no" >> /etc/ssh/sshd_config && \
    echo "    AllowTcpForwarding no" >> /etc/ssh/sshd_config && \
    echo "    X11Forwarding no" >> /etc/ssh/sshd_config


# Expose the SSH port
EXPOSE 22

# Run SSHD on container start
CMD ["/usr/sbin/sshd", "-D", "-e"]